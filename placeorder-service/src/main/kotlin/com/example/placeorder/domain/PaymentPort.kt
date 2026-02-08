package com.example.placeorder.domain

import com.example.contract.AuthorizePaymentCommand
import com.example.contract.AuthorizePaymentResult

interface PaymentPort {
    fun authorize(command: AuthorizePaymentCommand): AuthorizePaymentResult
}
