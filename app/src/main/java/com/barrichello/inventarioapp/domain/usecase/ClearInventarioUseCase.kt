package com.barrichello.inventarioapp.domain.usecase

import com.barrichello.inventarioapp.data.local.preferences.InventoryPreferences
import com.barrichello.inventarioapp.domain.repository.InventarioRepository
import javax.inject.Inject

class ClearInventarioUseCase @Inject constructor(
    private val repository: InventarioRepository,
    private val preferences: InventoryPreferences
) {
    suspend operator fun invoke() {
        repository.clearAll()
        preferences.clearLastLocation()
    }
}