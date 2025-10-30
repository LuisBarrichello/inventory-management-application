package com.barrichello.inventarioapp.core.barcode

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.atomic.AtomicBoolean

class BarcodeAnalyzer(
    private val onBarcodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient()

    private val isScanning = AtomicBoolean(true)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        if (!isScanning.get()) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        val barcode = barcodes.firstNotNullOfOrNull { it.rawValue }
                        if (barcode != null) {
                            pause()
                            onBarcodeScanned(barcode)
                        }
                    }
                }
                .addOnFailureListener { TODO() }
                .addOnCompleteListener { imageProxy.close() }
        }
    }

    fun pause() {
        isScanning.set(false)
    }

    fun resume() {
        isScanning.set(true)
    }
}