package com.barrichello.inventarioapp.ui.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barrichello.inventarioapp.core.barcode.ExtractedData
import com.barrichello.inventarioapp.data.local.preferences.InventoryPreferences
import com.barrichello.inventarioapp.domain.usecase.AddItemResult
import com.barrichello.inventarioapp.domain.usecase.AdditemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ScannerUiState(
    val scannedBarcode: String? = null,
    val coilId: String = "",
    val weight: String = "",
    val thickness: String = "",
    val quality: String = "",
    val color: String = "",
    val location: String = "",
    val observation: String = "",
    val isDuplicate: Boolean = false,
    val status: String = "FECHADA"
)

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val addItemUseCase: AdditemUseCase,
    private val preferences: InventoryPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    fun onConfirmItem() {
        val state = _uiState.value
        val code = state.scannedBarcode ?: return

        val rawLoc = state.location.uppercase()
        val finalLocation = if (rawLoc.startsWith("F-")) rawLoc else "F-$rawLoc"

        viewModelScope.launch {
            val result = addItemUseCase.invoke(
                barcode = code,
                coilId = state.coilId,
                weight = state.weight,
                thickness = state.thickness,
                quality = state.quality,
                color = state.color,
                location = finalLocation,
                observation = state.observation,
                status = state.status,
                forceUpdate = state.isDuplicate
            )

            when (result) {
                is AddItemResult.Success -> {
                    if (finalLocation.isNotBlank()) {
                        preferences.saveLastLocation(finalLocation)
                    }
                    preferences.saveLastStatus(state.status)
                    dismissBottomSheet()
                }
                is AddItemResult.Duplicate -> _uiState.update { it.copy(isDuplicate = true) }
            }
        }
    }

    fun dismissBottomSheet() {
        _uiState.update { ScannerUiState(scannedBarcode = null) }
    }

    fun onResultFound(barcode: String, data: ExtractedData) {
        val cleanBarcode = barcode.trimStart('0').ifEmpty { barcode }
        val savedLocation = preferences.getLastLocation()
        val savedStatus = preferences.getLastStatus()

        _uiState.update {
            it.copy(
                scannedBarcode = cleanBarcode,
                coilId = data.coilId,
                weight = data.weight,
                thickness = data.thickness,
                quality = data.quality,
                color = data.color,
                location = savedLocation,
                observation = "",
                isDuplicate = false,
                status = savedStatus
            )
        }
    }

    fun onBarcodeChanged(v: String) { _uiState.update { it.copy(scannedBarcode = v) } }
    fun onCoilIdChanged(v: String) { _uiState.update { it.copy(coilId = v) } }
    fun onWeightChanged(v: String) { _uiState.update { it.copy(weight = v) } }
    fun onThicknessChanged(v: String) { _uiState.update { it.copy(thickness = v) } }
    fun onQualityChanged(v: String) { _uiState.update { it.copy(quality = v) } }
    fun onColorChanged(v: String) { _uiState.update { it.copy(color = v) } }
    fun onLocationChanged(v: String) { _uiState.update { it.copy(location = v) } }
    fun onObservationChanged(v: String) { _uiState.update { it.copy(observation = v) } }

    fun onStartManualEntry() {
        val savedLocation = preferences.getLastLocation()
        val savedStatus = preferences.getLastStatus()
        _uiState.update {
            it.copy(
                scannedBarcode = "",
                coilId = "",
                weight = "",
                thickness = "",
                quality = "",
                color = "",
                location = savedLocation,
                observation = "",
                isDuplicate = false,
                status = savedStatus
            )
        }
    }

    fun onStatusChanged(v: String) { _uiState.update { it.copy(status = v) } }
}
