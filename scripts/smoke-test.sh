#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"

if ! command -v curl >/dev/null 2>&1; then
  echo "curl is required" >&2
  exit 1
fi

has_jq=true
if ! command -v jq >/dev/null 2>&1; then
  has_jq=false
fi

has_grpcurl=true
if ! command -v grpcurl >/dev/null 2>&1; then
  has_grpcurl=false
fi

kafka_container_id=$(docker ps -qf name=kafka | head -n 1)
if [ -z "${kafka_container_id}" ]; then
  echo "Kafka container not running. Start with: docker compose up --build" >&2
  exit 1
fi

echo "== HTTP test (PlaceOrder -> Inventory + Payment over HTTP) =="
http_raw=$(curl -s -X POST http://localhost:8080/orders \
  -H 'Content-Type: application/json' \
  -d '{"orderId":"o-1","items":[{"sku":"sku-1","quantity":2}]}')

if [ "${has_jq}" = true ]; then
  http_result=$(printf '%s' "${http_raw}" | jq -r '.accepted')
else
  http_result=$(printf '%s' "${http_raw}" | grep -Eo '\"accepted\"[[:space:]]*:[[:space:]]*(true|false)' | head -n1 | grep -Eo '(true|false)')
fi

if [ "${http_result}" != "true" ]; then
  echo "HTTP test failed" >&2
  exit 1
fi

echo "OK"

if [ "${has_grpcurl}" = true ]; then
  echo "== gRPC test (PlaceOrder -> Inventory + Payment over gRPC) =="
  grpc_raw=$(grpcurl -plaintext -d '{"orderId":"o-2","items":[{"sku":"sku-2","quantity":1}]}' \
    localhost:9090 com.example.proto.orders.OrderService/Place)

  if [ "${has_jq}" = true ]; then
    grpc_result=$(printf '%s' "${grpc_raw}" | jq -r '.accepted')
  else
    grpc_result=$(printf '%s' "${grpc_raw}" | grep -Eo 'accepted:[[:space:]]*(true|false)' | head -n1 | grep -Eo '(true|false)')
  fi

  if [ "${grpc_result}" != "true" ]; then
    echo "gRPC test failed" >&2
    exit 1
  fi

  echo "OK"
else
  echo "Skipping gRPC test (grpcurl not installed)"
fi

echo "== Kafka test (command + reply) =="
# Start a short-lived reply consumer in background
reply_file="${ROOT_DIR}/.tmp-kafka-reply.json"
rm -f "${reply_file}"

( docker exec -i "${kafka_container_id}" \
  kafka-console-consumer --bootstrap-server kafka:9092 \
  --topic orders.place.reply --from-beginning \
  --property print.headers=true --timeout-ms 5000 \
  | head -n 1 > "${reply_file}" ) &

sleep 1

# Send command with correlation header
printf '%s' '{"orderId":"o-3","items":[{"sku":"sku-3","quantity":1}]}' | \
  docker exec -i "${kafka_container_id}" \
  kafka-console-producer --broker-list kafka:9092 --topic orders.place.cmd \
  --property parse.key=true --property key.separator=: \
  --property headers=correlationId:cid-123 \
  <<< "o-3:{\"orderId\":\"o-3\",\"items\":[{\"sku\":\"sku-3\",\"quantity\":1}]}"

wait || true

if [ ! -s "${reply_file}" ]; then
  echo "Kafka test failed (no reply)" >&2
  exit 1
fi

echo "Reply:"
cat "${reply_file}"

echo "OK"

rm -f "${reply_file}"

echo "All smoke tests passed."
