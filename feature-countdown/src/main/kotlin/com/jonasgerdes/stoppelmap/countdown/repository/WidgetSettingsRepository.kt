package com.jonasgerdes.stoppelmap.countdown.repository

import android.content.SharedPreferences

class WidgetSettingsRepository<T>(
    private val sharedPreferences: SharedPreferences,
    private val loadFromPreferences: (SharedPreferences, Int) -> T,
    private val saveToPreferences: (SharedPreferences, T) -> Unit
) {

    fun loadSettings(appWidgetId: Int): T = loadFromPreferences(sharedPreferences, appWidgetId)

    fun saveSettings(settings: T) {
        saveToPreferences(sharedPreferences, settings)
    }
}
