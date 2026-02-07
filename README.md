# enterprise-sample

This is a sample project for Specmatic enterprise. It demonstrates how to use the features of the enterprise edition in a simple application.

## Running Contract Tests with Specmatic Enterprise

Pre-req: Make sure you have Docker and Docker Compose installed on your machine.

#### 1. Build the application
```shell
./gradlew :inventory-service:bootJar
./gradlew :placeorder-service:bootJar
```

```shell
docker compose -f docker-compose.test.yml up --build
```