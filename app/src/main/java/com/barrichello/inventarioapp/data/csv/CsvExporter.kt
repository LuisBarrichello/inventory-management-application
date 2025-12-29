package com.barrichello.inventarioapp.data.csv

import com.barrichello.inventarioapp.domain.model.InventarioItem
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class CsvExporter @Inject constructor() {
    private val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    fun export(items: List<InventarioItem>): String {
        val csvBuilder  = StringBuilder()

        csvBuilder.append("Etiqueta,ID G,Peso,Espessurra,Qualidade,Cor,Hora de leitura\n")

        items.forEach { item ->
            val formattedDate = sdf.format(item.lastUpdate)
            val cleanQuality = item.quality.replace(",", " ").replace("\n", " ")
            val cleanColor = item.color.replace(",", " ").replace("\n", " ")

            csvBuilder.append(
                "${item.barcode},${item.coilId},${item.weight},${item.thickness},$cleanQuality,$cleanColor,$formattedDate\n"
            )
        }

        return csvBuilder.toString()
    }
}