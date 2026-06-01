package com.barrichello.inventarioapp.data.local.preferences

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class InventoryPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("inventory_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LAST_LOCATION = "last_location"
        private const val KEY_LAST_STATUS = "last_status"
    }

    fun getLastLocation(): String {
        return prefs.getString(KEY_LAST_LOCATION, "") ?: ""
    }

    fun saveLastLocation(location: String) {
        val clearLocation = location.replace("F-", "")
        prefs.edit().putString(KEY_LAST_LOCATION, clearLocation).apply()
    }

    fun getLastStatus(): String {
        return prefs.getString(KEY_LAST_STATUS, "FECHADA") ?: "FECHADA"
    }

    fun saveLastStatus(status: String) {
        prefs.edit().putString(KEY_LAST_STATUS, status).apply()
    }

    fun clearLastLocation() {
        prefs.edit().remove(KEY_LAST_LOCATION).apply()
        prefs.edit().remove(KEY_LAST_STATUS).apply()
    }
}