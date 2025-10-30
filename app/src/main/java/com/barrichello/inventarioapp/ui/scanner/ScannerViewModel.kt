package com.barrichello.inventarioapp.ui.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val scannedCode: String? = null,
    val isDuplicate: Boolean = false
)

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val addItemUseCase: AdditemUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()
    fun onBarcodeScanned(code: String) {
        _uiState.update {
            it.copy(
                scannedCode = code,
                isDuplicate = false
            )
        }
    }

    fun onConfirmItem() {
        val code = _uiState.value.scannedCode ?: return

        viewModelScope.launch {
            when (addItemUseCase(codigo = code)) {
                is AddItemResult.Success -> {
                    dismissBottomSheet()
                }
                is AddItemResult.Duplicate -> {
                    _uiState.update { it.copy(isDuplicate = true) }
                }
            }
        }
    }

    fun dismissBottomSheet() {
        _uiState.update {
            it.copy(scannedCode = null, isDuplicate = false)
        }
    }
}
