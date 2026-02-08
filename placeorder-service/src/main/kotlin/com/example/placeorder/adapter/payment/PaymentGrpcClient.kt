package com.example.placeorder.adapter.payment

import com.example.contract.AuthorizePaymentCommand
import com.example.contract.AuthorizePaymentResult
import com.example.placeorder.domain.PaymentPort
import com.example.proto.payment.AuthorizeRequest
import com.example.proto.payment.PaymentServiceGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class PaymentGrpcClient : PaymentPort {

    @GrpcClient("payment")
    private lateinit var stub: PaymentServiceGrpc.PaymentServiceBlockingStub

    override fun authorize(command: AuthorizePaymentCommand): AuthorizePaymentResult {
        val request = AuthorizeRequest.newBuilder()
            .setOrderId(command.orderId)
            .addAllItems(command.items.map { item ->
                com.example.proto.payment.PaymentItem.newBuilder()
                    .setSku(item.sku)
                    .setQuantity(item.quantity)
                    .build()
            })
            .build()
        val reply = stub.authorize(request)
        return AuthorizePaymentResult(reply.authorized, reply.reason.ifBlank { null })
    }
}
