package com.example.contract

data class OrderItem(
    val sku: String,
    val quantity: Int
)

data class PlaceOrderCommand(
    val orderId: String,
    val items: List<OrderItem>
)

data class PlaceOrderResult(
    val accepted: Boolean,
    val reason: String? = null
) {
    companion object {
        fun accepted(orderId: String) = PlaceOrderResult(true, null)
        fun rejected(orderId: String, reason: String) = PlaceOrderResult(false, reason)
    }
}

data class ReserveInventoryCommand(
    val orderId: String,
    val items: List<OrderItem>
)

data class ReserveInventoryResult(
    val accepted: Boolean,
    val reason: String? = null
)
