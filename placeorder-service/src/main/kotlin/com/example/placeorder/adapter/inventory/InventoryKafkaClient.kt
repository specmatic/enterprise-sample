package com.example.placeorder.adapter.inventory

import com.example.contract.ReserveInventoryCommand
import com.example.contract.ReserveInventoryResult
import com.example.placeorder.adapter.kafka.KafkaReplyRegistry
import com.example.placeorder.domain.InventoryPort
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import java.util.*
import java.util.concurrent.TimeUnit

class InventoryKafkaClient(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    private val replyRegistry: KafkaReplyRegistry
) : InventoryPort {
    override fun reserve(command: ReserveInventoryCommand): ReserveInventoryResult {
        val correlationId = UUID.randomUUID().toString()
        val payload = objectMapper.writeValueAsString(command)
        val record = ProducerRecord("inventory.reserve.cmd", command.orderId, payload)
        record.headers().add("correlationId", correlationId.toByteArray())
        kafkaTemplate.send(record)

        val json = replyRegistry.await("inventory.reserve.reply", correlationId, 3, TimeUnit.SECONDS)
            ?: return ReserveInventoryResult(false, "Inventory timeout")
        return objectMapper.readValue(json, ReserveInventoryResult::class.java)
    }
}
