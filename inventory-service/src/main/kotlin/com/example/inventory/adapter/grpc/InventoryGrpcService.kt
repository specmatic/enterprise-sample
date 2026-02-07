package com.example.inventory.adapter.grpc

import com.example.contract.OrderItem
import com.example.contract.ReserveInventoryCommand
import com.example.inventory.domain.ReserveInventory
import com.example.proto.inventory.InventoryServiceGrpc
import com.example.proto.inventory.ReserveReply
import com.example.proto.inventory.ReserveRequest
import net.devh.boot.grpc.server.service.GrpcService
import io.grpc.stub.StreamObserver

@GrpcService
class InventoryGrpcService(
    private val useCase: ReserveInventory
) : InventoryServiceGrpc.InventoryServiceImplBase() {

    override fun reserve(request: ReserveRequest, responseObserver: StreamObserver<ReserveReply>) {
        val command = ReserveInventoryCommand(
            orderId = request.orderId,
            items = request.itemsList.map { OrderItem(it.sku, it.quantity) }
        )
        val result = useCase.execute(command)
        val reply = ReserveReply.newBuilder()
            .setAccepted(result.accepted)
            .setReason(result.reason ?: "")
            .build()
        responseObserver.onNext(reply)
        responseObserver.onCompleted()
    }
}
