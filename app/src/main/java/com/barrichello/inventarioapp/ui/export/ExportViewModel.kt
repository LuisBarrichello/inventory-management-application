package com.barrichello.inventarioapp.ui.export

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barrichello.inventarioapp.domain.usecase.ExportCsvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExportUiState(
    val isLoading: Boolean = false
)

sealed class ExportEvent {
    data class Success(val uri: Uri) : ExportEvent()
    data class Error(val message: String) : ExportEvent()
}

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val exportCsvUseCase: ExportCsvUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportUiState())
    val uiState: StateFlow<ExportUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ExportEvent>()
    val eventFlow: SharedFlow<ExportEvent> = _eventFlow.asSharedFlow()

    fun onExportClicked() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = exportCsvUseCase()

            result.onSuccess { uri ->
                _eventFlow.emit(ExportEvent.Success(uri))
            }
            result.onFailure { error ->
                _eventFlow.emit(ExportEvent.Error(error.message ?: "Erro desconhecido"))
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}