package com.example.placeorder.adapter.kafka

import com.example.contract.PlaceOrderCommand
import com.example.contract.PlaceOrderResult
import com.example.placeorder.domain.PlaceOrder
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class PlaceOrderKafkaHandler(
    @Qualifier("placeOrderKafka") private val useCase: PlaceOrder,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {
    @KafkaListener(topics = ["orders.place.cmd"], groupId = "orders-service")
    fun handle(record: ConsumerRecord<String, String>) {
        val correlationId = record.headers()
            .lastHeader("correlationId")
            ?.value()
            ?.toString(Charsets.UTF_8)

        val command = objectMapper.readValue(record.value(), PlaceOrderCommand::class.java)
        val result = useCase.execute(command)

        sendReply(record.key(), correlationId, result)
    }

    private fun sendReply(key: String?, correlationId: String?, result: PlaceOrderResult) {
        val json = objectMapper.writeValueAsString(result)
        val reply = ProducerRecord<String, String>("orders.place.reply", key, json)
        if (correlationId != null) {
            reply.headers().add("correlationId", correlationId.toByteArray())
        }
        kafkaTemplate.send(reply)
    }
}
