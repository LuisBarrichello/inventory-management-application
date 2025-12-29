package com.barrichello.inventarioapp.data.csv

import com.barrichello.inventarioapp.domain.model.InventarioItem
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class CsvExporter @Inject constructor() {
    private val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    fun export(items: List<InventarioItem>): String {
        val csvBuilder  = StringBuilder()

        csvBuilder.append("Etiqueta;ID G;Peso;Espessura;Qualidade;Cor;Localizacao;Hora de leitura\n")

        items.forEach { item ->
            val formattedDate = sdf.format(item.lastUpdate)

            val cleanQuality = item.quality.replace(";", " ").replace("\n", " ").trim()
            val cleanColor = item.color.replace(";", " ").replace("\n", " ").trim()
            val cleanLocation = item.location.replace(";", " ").trim()

            val formattedWeight = item.weight
                .replace(".", "")
                .replace(",", ".")
                .toDoubleOrNull()?.toInt()?.toString() ?: item.weight

            csvBuilder.append(
                "${item.barcode};${item.coilId};$formattedWeight;${item.thickness};$cleanQuality;$cleanColor;$cleanLocation;$formattedDate\n"
            )
        }

        return csvBuilder.toString()
    }
}