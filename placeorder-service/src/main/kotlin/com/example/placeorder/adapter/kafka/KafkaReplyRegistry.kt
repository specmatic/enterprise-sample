package com.example.placeorder.adapter.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Component
class KafkaReplyRegistry {
    private val pending = ConcurrentHashMap<String, CompletableFuture<String>>()

    @KafkaListener(
        topics = ["inventory.reserve.reply", "payment.authorize.reply", "orders.place.reply"],
        groupId = "orders-replies"
    )
    fun handle(record: ConsumerRecord<String, String>) {
        val correlationId = record.headers()
            .lastHeader("correlationId")
            ?.value()
            ?.toString(Charsets.UTF_8)
            ?: return
        pending.remove(correlationId)?.complete(record.value())
    }

    fun await(topic: String, correlationId: String, timeout: Long, unit: TimeUnit): String? {
        val future = CompletableFuture<String>()
        pending[correlationId] = future
        return try {
            future.get(timeout, unit)
        } catch (ex: Exception) {
            null
        } finally {
            pending.remove(correlationId)
        }
    }
}
