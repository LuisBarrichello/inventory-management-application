package com.barrichello.inventarioapp.domain.usecase

import com.barrichello.inventarioapp.domain.model.InventarioItem
import com.barrichello.inventarioapp.domain.repository.InventarioRepository
import javax.inject.Inject

sealed class AddItemResult {
   object Success : AddItemResult()
   object Duplicate : AddItemResult()
}

class AdditemUseCase @Inject constructor(
   private  val repository: InventarioRepository
) {
   suspend operator fun invoke(
      barcode: String,
      coilId: String,
      weight: String,
      thickness: String,
      quality: String,
      color: String
   ): AddItemResult {
      val itemExisting = repository.getItemByCodigo(barcode)

      if (itemExisting != null) {
         return AddItemResult.Duplicate
      }

      val itemToSave = InventarioItem(
         barcode = barcode,
         coilId = coilId,
         weight = weight,
         thickness = thickness,
         quality = quality,
         color = color,
         lastUpdate = System.currentTimeMillis()
      )

      repository.upsertItem(itemToSave)
      return AddItemResult.Success
   }
}