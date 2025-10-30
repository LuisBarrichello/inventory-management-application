package com.barrichello.inventarioapp.data.local.db // Pacote corrigido

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventario_items")
data class InventarioItemEntity(
    @PrimaryKey
    val codigo: String,
    val ultimoUpdate: Long
)