package com.barrichello.inventarioapp.domain.model

data class InventarioItem (
    val barcode: String,
    val coilId: String,
    val weight: String,
    val thickness: String,
    val quality: String,
    val color: String,
    val location: String,
    val lastUpdate: Long
)