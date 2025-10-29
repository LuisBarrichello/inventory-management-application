package com.barrichello.inventarioapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InventarioDao {

    @Query("SELECT * FROM inventario_items ORDER BY ultimoUpdate DESC")
    fun getAllItems(): Flow<List<InventarioItemEntity>>

    @Query("SELECT SUM(quantidade) FROM inventario_items")
    fun getTotalItemCount(): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItem(item: InventarioItemEntity)
}