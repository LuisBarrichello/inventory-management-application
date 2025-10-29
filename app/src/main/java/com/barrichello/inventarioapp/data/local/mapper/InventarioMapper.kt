package com.barrichello.inventarioapp.data.local.mapper

import com.barrichello.inventarioapp.data.local.db.InventarioItemEntity
import com.barrichello.inventarioapp.domain.model.InventarioItem

fun InventarioItemEntity.toDomain(): InventarioItem {
    return InventarioItem(
        codigo = this.codigo,
        quantidade = this.quantidade,
        ultimoUpdate = this.ultimoUpdate
    )
}

fun InventarioItem.toEntity(): InventarioItemEntity {
    return InventarioItemEntity(
        codigo = this.codigo,
        quantidade = this.quantidade,
        ultimoUpdate = this.ultimoUpdate
    )
}