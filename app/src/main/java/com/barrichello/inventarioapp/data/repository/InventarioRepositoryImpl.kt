package com.barrichello.inventarioapp.data.repository

import com.barrichello.inventarioapp.data.local.datasource.LocalInventarioDataSource
import com.barrichello.inventarioapp.data.local.db.InventarioItemEntity
import com.barrichello.inventarioapp.domain.repository.InventarioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventarioRepositoryImpl @Inject constructor(
    private val localDataSource: LocalInventarioDataSource
) : InventarioRepository {

    override fun getAllItems(): Flow<List<InventarioItemEntity>> {
        return localDataSource.getAllItems()
    }

    override fun getTotalItemCount(): Flow<Int?> {
        return localDataSource.getTotalItemCount()
    }

    override suspend fun upsertItem(item: InventarioItemEntity) {
        localDataSource.upsertItem(item)
    }
}