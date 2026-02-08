package com.example.placeorder.config

import com.example.placeorder.adapter.inventory.InventoryGrpcClient
import com.example.placeorder.adapter.inventory.InventoryHttpClient
import com.example.placeorder.adapter.inventory.InventoryKafkaClient
import com.example.placeorder.adapter.kafka.KafkaReplyRegistry
import com.example.placeorder.domain.InventoryPort
import com.example.placeorder.domain.PlaceOrder
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.client.RestClient

@Configuration
class PlaceOrderConfig {

    @Bean
    fun inventoryRestClient(@Value($$"${inventory.http.base-url}") baseUrl: String): RestClient {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .build()
    }

    @Bean
    fun inventoryHttpPort(restClient: RestClient): InventoryPort = InventoryHttpClient(restClient)

    @Bean
    fun inventoryGrpcPort(grpcClient: InventoryGrpcClient): InventoryPort = grpcClient

    @Bean
    fun inventoryKafkaPort(
        kafkaTemplate: KafkaTemplate<String, String>,
        objectMapper: ObjectMapper,
        replyRegistry: KafkaReplyRegistry
    ): InventoryPort = InventoryKafkaClient(kafkaTemplate, objectMapper, replyRegistry)

    @Bean
    @Qualifier("placeOrderHttp")
    fun placeOrderHttp(@Qualifier("inventoryHttpPort") inventoryPort: InventoryPort) = PlaceOrder(inventoryPort)

    @Bean
    @Qualifier("placeOrderGrpc")
    fun placeOrderGrpc(@Qualifier("inventoryGrpcPort") inventoryPort: InventoryPort) = PlaceOrder(inventoryPort)

    @Bean
    @Qualifier("placeOrderKafka")
    fun placeOrderKafka(@Qualifier("inventoryKafkaPort") inventoryPort: InventoryPort) = PlaceOrder(inventoryPort)
}
