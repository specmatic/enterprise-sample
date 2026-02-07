package com.example.inventory.domain

import com.example.contract.ReserveInventoryCommand
import com.example.contract.ReserveInventoryResult
import org.springframework.stereotype.Service

@Service
class ReserveInventory {
    fun execute(command: ReserveInventoryCommand): ReserveInventoryResult {
        if (command.items.isEmpty()) {
            return ReserveInventoryResult(false, "No items supplied")
        }
        if (command.items.any { it.quantity <= 0 }) {
            return ReserveInventoryResult(false, "Invalid quantity")
        }
        return ReserveInventoryResult(true, null)
    }
}
