package com.example.inventory.adapter.http

import com.example.contract.ReserveInventoryCommand
import com.example.contract.ReserveInventoryResult
import com.example.inventory.domain.ReserveInventory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/inventory")
class InventoryHttpController(
    private val useCase: ReserveInventory
) {
    @PostMapping("/reserve")
    fun reserve(@RequestBody command: ReserveInventoryCommand): ReserveInventoryResult =
        useCase.execute(command)
}
