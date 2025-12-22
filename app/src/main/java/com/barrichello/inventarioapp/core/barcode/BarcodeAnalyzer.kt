package com.barrichello.inventarioapp.core.barcode

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
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
                        textRecognizer.process(image)
                            .addOnSuccessListener { visionText ->
                                val extractedData = parseLabelText(visionText.text)
                                pause()
                                onResultFound(barcode, extractedData)
                            }
                   }
                }
            }
            .addOnCompleteListener { imageProxy.close() }
    }

    private fun parseLabelText(fullText: String): ExtractedData {
        val coilIdPattern = Regex("""\b\d{5}\b""")
        val weightPattern = Regex("""(?i)PESO:?\s*([\d\.,]+)""")
        val thicknessPattern = Regex("""(?i)ESPESSURA:?\s*([\d\.,]+)""")
        val qualityPattern = Regex("""(?i)QUALIDADE:?\s*(.+)""")
        val colorPattern = Regex("""(?i)COR:?\s*(.+)""")
        val coilId = coilIdPattern.find(fullText)?.value ?: ""

        val weight = weightPattern.find(fullText)?.groupValues?.get(1) ?: ""
        val thickness = thicknessPattern.find(fullText)?.groupValues?.get(1) ?: ""
        val quality = qualityPattern.find(fullText)?.groupValues?.get(1)?.trim() ?: ""
        val color = colorPattern.find(fullText)?.groupValues?.get(1)?.trim() ?: ""

        return ExtractedData(
            coilId = coilId,
            weight = weight,
            thickness = thickness,
            quality = quality,
            color = color
        )
    }

    fun pause() {
        isScanning.set(false)
    }

    fun resume() {
        isScanning.set(true)
    }
}