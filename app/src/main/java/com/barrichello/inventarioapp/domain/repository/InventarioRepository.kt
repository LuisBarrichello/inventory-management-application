package com.barrichello.inventarioapp.domain.repository

import com.barrichello.inventarioapp.data.local.db.InventarioItemEntity
import kotlinx.coroutines.flow.Flow

interface InventarioRepository {

    fun getAllItems(): Flow<List<InventarioItemEntity>>

    fun getTotalItemCount(): Flow<Int?>

    suspend fun upsertItem(item: InventarioItemEntity)
}