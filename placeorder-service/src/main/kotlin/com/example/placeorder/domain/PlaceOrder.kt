package com.example.placeorder.domain

import com.example.contract.PlaceOrderCommand
import com.example.contract.PlaceOrderResult
import com.example.contract.ReserveInventoryCommand

class PlaceOrder(
    private val inventoryPort: InventoryPort
) {
    fun execute(command: PlaceOrderCommand): PlaceOrderResult {
        val inventoryResult = inventoryPort.reserve(
            ReserveInventoryCommand(command.orderId, command.items)
        )
        return if (inventoryResult.accepted) {
            PlaceOrderResult(true, null)
        } else {
            PlaceOrderResult(false, inventoryResult.reason ?: "Inventory rejected")
        }
    }
}
