package com.barrichello.inventarioapp.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.barrichello.inventarioapp.data.csv.CsvExporter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ExportCsvUseCase @Inject constructor(
    private val getInventarioUseCase: GetInventarioUseCase,
    private val csvExporter: CsvExporter,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke() : Result<Uri> {
        return withContext(Dispatchers.IO) {
            try {
                val items = getInventarioUseCase().first()

                if (items.isEmpty()) {
                    return@withContext Result.failure(Exception("O inventário está vazio. Não há nada para exportar."))
                }

                val csvString = csvExporter.export(items)

                val file = File(
                    context.cacheDir,
                    "inventario_bobinas_${System.currentTimeMillis()}.csv"
                )
                file.writeText(csvString)

                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )

                Result.success(uri)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}