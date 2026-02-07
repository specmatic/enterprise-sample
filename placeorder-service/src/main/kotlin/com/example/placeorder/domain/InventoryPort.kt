package com.example.placeorder.domain

import com.example.contract.ReserveInventoryCommand
import com.example.contract.ReserveInventoryResult

interface InventoryPort {
    fun reserve(command: ReserveInventoryCommand): ReserveInventoryResult
}
