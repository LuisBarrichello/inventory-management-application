package com.barrichello.inventarioapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.barrichello.inventarioapp.ui.dashboard.DashboardScreen
import com.barrichello.inventarioapp.ui.export.ExportScreen
import com.barrichello.inventarioapp.ui.scanner.ScannerScreen
import com.barrichello.inventarioapp.ui.stocklist.StockListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Dashboard.route
    ) {
        composable(AppScreens.Dashboard.route) {
            DashboardScreen(
                onNavigateToScanner = {
                    navController.navigate(AppScreens.Scanner.route)
                },
                onNavigateToStockList = {
                    navController.navigate(AppScreens.StockList.route)
                },
                onNavigateToExport = {
                    navController.navigate(AppScreens.Export.route)
                }
            )
        }

        composable(AppScreens.Scanner.route) {
            ScannerScreen()
        }

        composable(AppScreens.StockList.route) {
            StockListScreen()
        }

        composable(AppScreens.Export.route) {
            ExportScreen()
        }
    }
}