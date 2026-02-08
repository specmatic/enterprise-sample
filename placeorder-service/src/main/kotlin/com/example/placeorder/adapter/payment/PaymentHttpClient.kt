package com.example.placeorder.adapter.payment

import com.example.contract.AuthorizePaymentCommand
import com.example.contract.AuthorizePaymentResult
import com.example.placeorder.domain.PaymentPort
import org.springframework.web.client.RestClient

class PaymentHttpClient(
    private val restClient: RestClient
) : PaymentPort {
    override fun authorize(command: AuthorizePaymentCommand): AuthorizePaymentResult {
        return restClient.post()
            .uri("/payments/authorize")
            .body(command)
            .retrieve()
            .body(AuthorizePaymentResult::class.java)
            ?: AuthorizePaymentResult(false, "No response from payment")
    }
}
