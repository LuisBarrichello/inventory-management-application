package com.barrichello.inventarioapp.ui.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DashboardScreen(
    onNavigateToScanner: () -> Unit,
    onNavigateToStockList: () -> Unit,
    onNavigateToExport: () -> Unit
) {
    Text(text = "Dashboard Placeholder")
}