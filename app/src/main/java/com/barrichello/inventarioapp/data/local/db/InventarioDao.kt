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

    @Query("SELECT COUNT() FROM inventario_items")
    fun getTotalItemCount(): Flow<Int?>

    @Query("SELECT * FROM inventario_items WHERE barcode = :codigo LIMIT 1")
    suspend fun getItemByCodigo(codigo: String): InventarioItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItem(item: InventarioItemEntity)

    @Query("DELETE FROM inventario_items WHERE barcode = :codigo")
    suspend fun deleteItem(codigo: String)

    @Query("DELETE FROM inventario_items")
    suspend fun clearAll()
}