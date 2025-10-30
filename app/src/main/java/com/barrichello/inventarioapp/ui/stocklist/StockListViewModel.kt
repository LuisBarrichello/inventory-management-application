package com.barrichello.inventarioapp.ui.stocklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barrichello.inventarioapp.domain.model.InventarioItem
import com.barrichello.inventarioapp.domain.usecase.ClearInventarioUseCase
import com.barrichello.inventarioapp.domain.usecase.DeleteItemUseCase
import com.barrichello.inventarioapp.domain.usecase.GetInventarioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StockListUiState(
    val showClearConfirmDialog: Boolean = false
)

@HiltViewModel
class StockListViewModel @Inject constructor(
    getInventarioUseCase: GetInventarioUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val clearInventarioUseCase: ClearInventarioUseCase
) : ViewModel() {
    val itemList: StateFlow<List<InventarioItem>> = getInventarioUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500L),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(StockListUiState())
    val uiState: StateFlow<StockListUiState> = _uiState.asStateFlow()

    fun onDeleteItem(codigo: String) {
        viewModelScope.launch {
            deleteItemUseCase(codigo)
        }
    }

    fun onClearAllClicked() {
        _uiState.update { it.copy(showClearConfirmDialog = true) }
    }

    fun onClearAllConfirmed() {
        viewModelScope.launch {
            clearInventarioUseCase()
        }
        dismissClearDialog()
    }

    fun onClearAllDismissed() {
        dismissClearDialog()
    }

    private fun dismissClearDialog() {
        _uiState.update { it.copy(showClearConfirmDialog = false) }
    }

}