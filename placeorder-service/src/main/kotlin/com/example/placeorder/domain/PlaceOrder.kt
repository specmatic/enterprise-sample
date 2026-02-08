package com.example.placeorder.domain

import com.example.contract.AuthorizePaymentCommand
import com.example.contract.PlaceOrderCommand
import com.example.contract.PlaceOrderResult
import com.example.contract.ReserveInventoryCommand

class PlaceOrder(
    private val inventoryPort: InventoryPort, private val paymentPort: PaymentPort
) {
    fun execute(command: PlaceOrderCommand): PlaceOrderResult {
        inventoryPort.reserve(
            ReserveInventoryCommand(command.orderId, command.items)
        )
        val paymentResult = paymentPort.authorize(
            AuthorizePaymentCommand(command.orderId, command.items)
        )
        return if (paymentResult.authorized) {
            PlaceOrderResult(true, null)
        } else {
            PlaceOrderResult(false, paymentResult.reason ?: "Payment rejected")
        }
    }
}
