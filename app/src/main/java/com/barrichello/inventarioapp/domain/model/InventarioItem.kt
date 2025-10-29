package com.barrichello.inventarioapp.domain.model

data class InventarioItem (
    val codigo: String,
    val quantidade: Int,
    val ultimoUpdate: Long
)