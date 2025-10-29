package com.barrichello.inventarioapp.domain.usecase

import com.barrichello.inventarioapp.domain.repository.InventarioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalItemCountUseCase @Inject constructor(
    private val repository: InventarioRepository
) {
    operator fun invoke(): Flow<Int?> {
        return repository.getTotalItemCount()
    }
}