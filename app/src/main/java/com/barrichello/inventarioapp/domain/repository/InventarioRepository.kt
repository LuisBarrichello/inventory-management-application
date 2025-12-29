package com.barrichello.inventarioapp.domain.repository

import com.barrichello.inventarioapp.domain.model.InventarioItem
import kotlinx.coroutines.flow.Flow

interface InventarioRepository {

    fun getAllItems(): Flow<List<InventarioItem>>

    fun getTotalItemCount(): Flow<Int?>

    suspend fun upsertItem(item: InventarioItem)

    suspend fun getItemByBarcode(codigo: String): InventarioItem?

    suspend fun getItemByCoilId(coilId: String): InventarioItem?

    suspend fun deleteItem(codigo: String)

    suspend fun clearAll()
}