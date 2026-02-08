package com.example.placeorder.adapter.http

import com.example.contract.PlaceOrderCommand
import com.example.contract.PlaceOrderResult
import com.example.placeorder.domain.PlaceOrder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class PlaceOrderHttpController(
    @param:Qualifier("placeOrderHttp") private val useCase: PlaceOrder
) {
    @PostMapping
    fun place(@RequestBody command: PlaceOrderCommand): PlaceOrderResult =
        useCase.execute(command)
}
