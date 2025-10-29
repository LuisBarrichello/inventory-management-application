package com.barrichello.inventarioapp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barrichello.inventarioapp.domain.usecase.GetTotalItemCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getTotalItemCountUseCase: GetTotalItemCountUseCase
) : ViewModel() {

    val totalItemCount: StateFlow<Int> = getTotalItemCountUseCase()
        .map { count ->
            count ?: 0
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )
}