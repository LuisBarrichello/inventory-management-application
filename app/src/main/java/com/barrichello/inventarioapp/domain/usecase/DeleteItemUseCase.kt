package com.barrichello.inventarioapp.domain.usecase

import com.barrichello.inventarioapp.domain.repository.InventarioRepository
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(
    private val repository: InventarioRepository
) {
    suspend operator fun invoke(codigo: String) {
        repository.deleteItem(codigo)
    }
}