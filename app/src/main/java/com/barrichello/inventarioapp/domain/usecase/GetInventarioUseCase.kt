package com.barrichello.inventarioapp.domain.usecase

import com.barrichello.inventarioapp.domain.model.InventarioItem
import com.barrichello.inventarioapp.domain.repository.InventarioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInventarioUseCase @Inject constructor(
    private val repository: InventarioRepository
) {
    operator fun invoke(): Flow<List<InventarioItem>> {
        return repository.getAllItems()
    }
}