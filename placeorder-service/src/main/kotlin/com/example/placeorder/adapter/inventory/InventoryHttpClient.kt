package com.example.placeorder.adapter.inventory

import com.example.contract.ReserveInventoryCommand
import com.example.contract.ReserveInventoryResult
import com.example.placeorder.domain.InventoryPort
import org.springframework.web.client.RestClient

class InventoryHttpClient(
    private val restClient: RestClient
) : InventoryPort {
    override fun reserve(command: ReserveInventoryCommand): ReserveInventoryResult {
        return restClient.post()
            .uri("/inventory/reserve")
            .body(command)
            .retrieve()
            .body(ReserveInventoryResult::class.java)
            ?: ReserveInventoryResult(false, "No response from inventory")
    }
}
