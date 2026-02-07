package com.example.inventory.adapter.kafka

import com.example.contract.ReserveInventoryCommand
import com.example.contract.ReserveInventoryResult
import com.example.inventory.domain.ReserveInventory
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class InventoryKafkaHandler(
    private val useCase: ReserveInventory,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {
    @KafkaListener(topics = ["inventory.reserve.cmd"], groupId = "inventory-service")
    fun handle(record: ConsumerRecord<String, String>) {
        val correlationId = record.headers()
            .lastHeader("correlationId")
            ?.value()
            ?.toString(Charsets.UTF_8)

        val command = objectMapper.readValue(record.value(), ReserveInventoryCommand::class.java)
        val result = useCase.execute(command)

        sendReply(record.key(), correlationId, result)
    }

    private fun sendReply(key: String?, correlationId: String?, result: ReserveInventoryResult) {
        val json = objectMapper.writeValueAsString(result)
        val reply = ProducerRecord<String, String>("inventory.reserve.reply", key, json)
        if (correlationId != null) {
            reply.headers().add("correlationId", correlationId.toByteArray())
        }
        kafkaTemplate.send(reply)
    }
}
