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
   suspend operator fun invoke(codigo: String): AddItemResult {
      val itemExisting = repository.getItemByCodigo(codigo)

      if (itemExisting != null) {
         return AddItemResult.Duplicate
      }

      val itemToSave = InventarioItem(
         codigo = codigo,
         lastUpdate = System.currentTimeMillis()
      )

      repository.upsertItem(itemToSave)
      return AddItemResult.Success
   }
}