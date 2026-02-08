package com.example.placeorder.adapter.grpc

import com.example.contract.OrderItem
import com.example.contract.PlaceOrderCommand
import com.example.placeorder.domain.PlaceOrder
import com.example.proto.orders.OrderServiceGrpc
import com.example.proto.orders.PlaceOrderReply
import com.example.proto.orders.PlaceOrderRequest
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.beans.factory.annotation.Qualifier

@GrpcService
class PlaceOrderGrpcService(
    @param:Qualifier("placeOrderGrpc") private val useCase: PlaceOrder
) : OrderServiceGrpc.OrderServiceImplBase() {

    override fun place(request: PlaceOrderRequest, responseObserver: StreamObserver<PlaceOrderReply>) {
        val command = PlaceOrderCommand(
            orderId = request.orderId,
            items = request.itemsList.map { OrderItem(it.sku, it.quantity) }
        )
        val result = useCase.execute(command)
        val reply = PlaceOrderReply.newBuilder()
            .setAccepted(result.accepted)
            .setReason(result.reason ?: "")
            .build()
        responseObserver.onNext(reply)
        responseObserver.onCompleted()
    }
}
