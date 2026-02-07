package com.example.placeorder.adapter.inventory

import com.example.contract.ReserveInventoryCommand
import com.example.contract.ReserveInventoryResult
import com.example.placeorder.domain.InventoryPort
import com.example.proto.inventory.InventoryServiceGrpc
import com.example.proto.inventory.ReserveRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class InventoryGrpcClient : InventoryPort {

    @GrpcClient("inventory")
    private lateinit var stub: InventoryServiceGrpc.InventoryServiceBlockingStub

    override fun reserve(command: ReserveInventoryCommand): ReserveInventoryResult {
        val request = ReserveRequest.newBuilder()
            .setOrderId(command.orderId)
            .addAllItems(command.items.map { item ->
                com.example.proto.inventory.InventoryItem.newBuilder()
                    .setSku(item.sku)
                    .setQuantity(item.quantity)
                    .build()
            })
            .build()
        val reply = stub.reserve(request)
        return ReserveInventoryResult(reply.accepted, reply.reason.ifBlank { null })
    }
}
