package com.barrichello.inventarioapp.data.repository

import com.barrichello.inventarioapp.data.local.datasource.LocalInventarioDataSource
import com.barrichello.inventarioapp.data.local.db.InventarioItemEntity
import com.barrichello.inventarioapp.data.local.mapper.toDomain
import com.barrichello.inventarioapp.data.local.mapper.toEntity
import com.barrichello.inventarioapp.domain.model.InventarioItem
import com.barrichello.inventarioapp.domain.repository.InventarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventarioRepositoryImpl @Inject constructor(
    private val localDataSource: LocalInventarioDataSource
) : InventarioRepository {

    override fun getAllItems(): Flow<List<InventarioItem>> {
        return localDataSource.getAllItems().map { entintyList -> entintyList.map { it.toDomain() } }
    }

    override fun getTotalItemCount(): Flow<Int?> {
        return localDataSource.getTotalItemCount()
    }

    override suspend fun upsertItem(item: InventarioItem) {
        localDataSource.upsertItem(item.toEntity())
    }
}