package com.barrichello.inventarioapp.domain.repository

import com.barrichello.inventarioapp.data.local.db.InventarioItemEntity
import com.barrichello.inventarioapp.domain.model.InventarioItem
import kotlinx.coroutines.flow.Flow

interface InventarioRepository {

    fun getAllItems(): Flow<List<InventarioItem>>

    fun getTotalItemCount(): Flow<Int?>

    suspend fun upsertItem(item: InventarioItem)

    suspend fun getItemByCodigo(codigo: String): InventarioItem?
}