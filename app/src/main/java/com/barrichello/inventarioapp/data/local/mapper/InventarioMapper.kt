package com.barrichello.inventarioapp.data.local.mapper

import com.barrichello.inventarioapp.data.local.db.InventarioItemEntity
import com.barrichello.inventarioapp.domain.model.InventarioItem

fun InventarioItemEntity.toDomain(): InventarioItem {
    return InventarioItem(
        barcode = this.barcode,
        coilId = this.coilId,
        weight = this.weight,
        thickness = this.thickness,
        quality = this.quality,
        color = this.color,
        lastUpdate = this.ultimoUpdate,
    )
}

fun InventarioItem.toEntity(): InventarioItemEntity {
    return InventarioItemEntity(
        barcode = this.barcode,
        coilId = this.coilId,
        weight = this.weight,
        thickness = this.thickness,
        quality = this.quality,
        color = this.color,
        ultimoUpdate = this.lastUpdate,
    )
}