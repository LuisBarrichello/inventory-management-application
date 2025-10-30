package com.barrichello.inventarioapp.data.local.mapper

import com.barrichello.inventarioapp.data.local.db.InventarioItemEntity
import com.barrichello.inventarioapp.domain.model.InventarioItem

fun InventarioItemEntity.toDomain(): InventarioItem {
    return InventarioItem(
        codigo = this.codigo,
        lastUpdate = this.ultimoUpdate
    )
}

fun InventarioItem.toEntity(): InventarioItemEntity {
    return InventarioItemEntity(
        codigo = this.codigo,
        ultimoUpdate = this.lastUpdate
    )
}