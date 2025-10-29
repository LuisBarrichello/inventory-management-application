package com.barrichello.inventarioapp.data.local.datasource

import com.barrichello.inventarioapp.data.local.db.InventarioDao
import com.barrichello.inventarioapp.data.local.db.InventarioItemEntity
import javax.inject.Inject

class LocalInventarioDataSource @Inject constructor(
    private val inventarioDao: InventarioDao
) {
    fun getAllItems() = inventarioDao.getAllItems()

    fun getTotalItemCount() = inventarioDao.getTotalItemCount()

    suspend fun upsertItem(item: InventarioItemEntity) = inventarioDao.upsertItem(item)
}