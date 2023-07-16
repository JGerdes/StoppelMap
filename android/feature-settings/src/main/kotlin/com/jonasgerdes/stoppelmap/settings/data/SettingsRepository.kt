package com.jonasgerdes.stoppelmap.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jonasgerdes.stoppelmap.settings.util.saveValueOf
import com.jonasgerdes.stoppelmap.theme.settings.ColorSchemeSetting
import com.jonasgerdes.stoppelmap.theme.settings.ThemeSetting
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val dataStore: DataStore<Preferences>,
) {

    private val themeSettingKey = stringPreferencesKey("theme")
    private val colorSchemeSettingKey = stringPreferencesKey("colorScheme")

    fun getSettings() = dataStore.data.map { preferences ->
        Settings(
            themeSetting = saveValueOf(preferences[themeSettingKey], ThemeSetting.Light),
            colorSchemeSetting = saveValueOf(
                preferences[colorSchemeSettingKey],
                ColorSchemeSetting.Classic
            )
        )
    }

    suspend fun saveThemeSetting(themeSetting: ThemeSetting) {
        dataStore.edit { settings ->
            settings[themeSettingKey] = themeSetting.name
        }
    }

    suspend fun saveColorSchemeSetting(colorSchemeSetting: ColorSchemeSetting) {
        dataStore.edit { settings ->
            settings[colorSchemeSettingKey] = colorSchemeSetting.name
        }
    }
}

data class Settings(
    val themeSetting: ThemeSetting,
    val colorSchemeSetting: ColorSchemeSetting,
)
