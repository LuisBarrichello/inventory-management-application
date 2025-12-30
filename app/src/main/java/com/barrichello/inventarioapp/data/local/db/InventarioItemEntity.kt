package com.barrichello.inventarioapp.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventario_items")
data class InventarioItemEntity(
    @PrimaryKey
    val barcode: String,
    val coilId: String,
    val weight: String,
    val thickness: String,
    val quality: String,
    val color: String,
    val location: String,
    val observation: String,
    val lastUpdate: Long
)