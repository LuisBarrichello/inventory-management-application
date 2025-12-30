package com.barrichello.inventarioapp.core.barcode

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.atomic.AtomicBoolean

data class ExtractedData (
    val coilId: String = "",
    val weight: String = "",
    val thickness: String = "",
    val quality: String = "",
    val color: String = ""
)

class BarcodeAnalyzer(
    private val onResultFound: (barcode: String, data: ExtractedData) -> Unit
) : ImageAnalysis.Analyzer {
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_EAN_13)
        .build()

    private val barcodeScanner = BarcodeScanning.getClient()
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val isScanning = AtomicBoolean(true)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        if (!isScanning.get()) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image ?: return
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    val barcode = barcodes.firstNotNullOfOrNull { it.rawValue }
                    if (barcode != null) {
                        processText(image, barcode, imageProxy)
                    } else {
                        imageProxy.close()
                    }
                } else {
                    imageProxy.close()
                }
            }

        .addOnFailureListener {
            Log.e("BarcodeAnalyzer", "Erro ao ler barcode", it)
            imageProxy.close()
        }
    }

    private fun processText(image: InputImage, barcode: String, imageProxy: ImageProxy) {
        textRecognizer.process(image)
            .addOnSuccessListener { visionText ->
                val extractedData = parseLabelText(visionText.text)

                Log.d("OCR_LEITURA", "----------------------------------------------------")
                Log.d("OCR_LEITURA", "TEXTO COMPLETO DETECTADO:\n$extractedData")
                Log.d("OCR_LEITURA", "----------------------------------------------------")

                pause()
                onResultFound(barcode, extractedData)
            }
            .addOnFailureListener { e ->
                Log.e("BarcodeAnalyzer", "Erro no OCR", e)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun parseLabelText(fullText: String): ExtractedData {
        val orderPattern = Regex("""(?i)PEDIDO\s+COMPRA[^0-9]*(\d+)""")
        val orderValue = orderPattern.find(fullText)?.groupValues?.get(1)

        val coilIdPattern = Regex("""\b\d{5}\b""")
        val candidates = coilIdPattern.findAll(fullText).map { it.value }
        val coilId = candidates.firstOrNull { it != orderValue } ?: ""

        val weightPattern = Regex("""(?i)PESO[^0-9]*([\d]+[.,][\d]+)""")
        val thicknessPattern = Regex("""(?i)ESPESSURA[^0-9]*([\d\.,]+)""")
        val qualityPattern = Regex("""(?i)QUALI\s*DA\s*DE[:\s\-\.]*(.+)""")
        val colorPattern = Regex("""(?i)COR[:\s\-\.]*(.+)""")

        Log.d("OCR_DEBUG", fullText)

        val rawWeight = weightPattern.find(fullText)?.groupValues?.get(1) ?: ""
        val rawThickness = thicknessPattern.find(fullText)?.groupValues?.get(1) ?: ""
        val quality = qualityPattern.find(fullText)?.groupValues?.get(1)?.trim() ?: ""
        val color = colorPattern.find(fullText)?.groupValues?.get(1)?.trim() ?: ""

        val weight = toBrazilianFormat(rawWeight)
        val thickness = toBrazilianFormat(rawThickness)

        return ExtractedData(coilId, weight, thickness, quality, color)
    }

    fun pause() {
        isScanning.set(false)
    }

    fun resume() {
        isScanning.set(true)
    }

    private fun toBrazilianFormat(value: String): String {
        if (value.isBlank()) return ""
        return value.replace(".", ",")
    }
}