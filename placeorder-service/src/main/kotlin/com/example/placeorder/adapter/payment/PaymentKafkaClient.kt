package com.example.placeorder.adapter.payment

import com.example.contract.AuthorizePaymentCommand
import com.example.contract.AuthorizePaymentResult
import com.example.placeorder.adapter.kafka.KafkaReplyRegistry
import com.example.placeorder.domain.PaymentPort
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import java.util.UUID
import java.util.concurrent.TimeUnit

class PaymentKafkaClient(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    private val replyRegistry: KafkaReplyRegistry
) : PaymentPort {
    override fun authorize(command: AuthorizePaymentCommand): AuthorizePaymentResult {
        val correlationId = UUID.randomUUID().toString()
        val payload = objectMapper.writeValueAsString(command)
        val record = ProducerRecord("payment.authorize.cmd", command.orderId, payload)
        record.headers().add("correlationId", correlationId.toByteArray())
        kafkaTemplate.send(record)

        val json = replyRegistry.await("payment.authorize.reply", correlationId, 3, TimeUnit.SECONDS)
            ?: return AuthorizePaymentResult(false, "Payment timeout")
        return objectMapper.readValue(json, AuthorizePaymentResult::class.java)
    }
}
