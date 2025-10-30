package com.barrichello.inventarioapp.data.csv

import com.barrichello.inventarioapp.domain.model.InventarioItem
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class CsvExporter @Inject constructor() {
    private val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    fun export(items: List<InventarioItem>): String {
        val csvBuilder  = StringBuilder()

        csvBuilder.append("Codigo, DataHoraContagem\n")

        items.forEach { item ->
            val formattedDate = sdf.format(item.lastUpdate)
            csvBuilder.append("${item.codigo}, $formattedDate\n")
        }

        return csvBuilder.toString()
    }
}