package com.barrichello.inventarioapp.data.local.datasource

import com.barrichello.inventarioapp.data.local.db.InventarioDao
import com.barrichello.inventarioapp.data.local.db.InventarioItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalInventarioDataSource @Inject constructor(
    private val inventarioDao: InventarioDao
) {
    fun getAllItems(): Flow<List<InventarioItemEntity>> {
        return inventarioDao.getAllItems()
    }

    fun getTotalItemCount(): Flow<Int?> {
        return inventarioDao.getTotalItemCount()
    }

    suspend fun upsertItem(item: InventarioItemEntity) {
        inventarioDao.upsertItem(item)
    }
}