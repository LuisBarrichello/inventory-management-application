package com.barrichello.inventarioapp.ui.navigation

sealed class AppScreens(val route: String) {
    object Dashboard : AppScreens("dashboard_screen")
    object Scanner : AppScreens("scanner_screen")
    object StockList : AppScreens("stocklist_screen")
    object Export : AppScreens("export_screen")
}