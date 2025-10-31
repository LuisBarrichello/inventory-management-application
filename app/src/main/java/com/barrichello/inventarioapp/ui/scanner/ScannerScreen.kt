package com.barrichello.inventarioapp.ui.scanner

import android.Manifest
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.barrichello.inventarioapp.core.barcode.BarcodeAnalyzer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (cameraPermissionState.status.isGranted) {
                ScannerContent(viewModel = viewModel)
            } else {
                PermissionRequestContent(
                    onRequestPermission = {
                        cameraPermissionState.launchPermissionRequest()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScannerContent(
    viewModel: ScannerViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()

    var hasFlashlight by remember { mutableStateOf(false) }
    var isFlashlightOn by remember { mutableStateOf(false) }
    val camera = remember { mutableStateOf<androidx.camera.core.Camera?>(null) }
    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> = remember {
        ProcessCameraProvider.getInstance(context)
    }
    val barcodeAnalyzer = remember {
        BarcodeAnalyzer { code -> viewModel.onBarcodeScanned(code) }
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showBottomSheet = uiState.scannedCode != null

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            barcodeAnalyzer.pause()
        } else {
            kotlinx.coroutines.delay(500)
            barcodeAnalyzer.resume()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(Executors.newSingleThreadExecutor(), barcodeAnalyzer)
                        }
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    try {
                        cameraProvider.unbindAll()
                        val cameraInstance = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                        camera.value = cameraInstance
                        hasFlashlight = cameraInstance.cameraInfo.hasFlashUnit()
                    } catch (e: Exception) {
                        Log.e("ScannerScreen", "Falha ao bindar CameraX", e)
                    }
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 300.dp, height = 150.dp)
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
            )
        }

        if (hasFlashlight) {
            IconButton(
                onClick = {
                    isFlashlightOn = !isFlashlightOn
                    camera.value?.cameraControl?.enableTorch(isFlashlightOn)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isFlashlightOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                    contentDescription = "Lanterna",
                    tint = Color.White
                )
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.dismissBottomSheet() },
            sheetState = sheetState
        ) {
            ScannerBottomSheetContent(
                scannedCode = uiState.scannedCode!!,
                isDuplicate = uiState.isDuplicate,
                onConfirm = { viewModel.onConfirmItem() },
                onCancel = { viewModel.dismissBottomSheet() }
            )
        }
    }
}

@Composable
private fun ScannerBottomSheetContent(
    scannedCode: String,
    isDuplicate: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Item Escaneado",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        Text(
            text = scannedCode,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))

        if (isDuplicate) {
            Text(
                text = "Esta bobina já foi contada.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text("CANCELAR", fontSize = 16.sp)
            }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = onConfirm,
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text(if (isDuplicate) "OK" else "CONFIRMAR", fontSize = 16.sp)
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun PermissionRequestContent(
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Permissão da Câmera Necessária",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Para escanear códigos de barras, precisamos de acesso à sua câmera.",
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRequestPermission) {
            Text("Conceder Permissão")
        }
    }
}