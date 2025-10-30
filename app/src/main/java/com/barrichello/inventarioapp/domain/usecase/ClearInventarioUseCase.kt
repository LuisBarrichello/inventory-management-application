package com.barrichello.inventarioapp.domain.usecase

import com.barrichello.inventarioapp.domain.repository.InventarioRepository
import javax.inject.Inject

class ClearInventarioUseCase @Inject constructor(
    private val repository: InventarioRepository
) {
    suspend operator fun invoke() {
        repository.clearAll()
    }
}