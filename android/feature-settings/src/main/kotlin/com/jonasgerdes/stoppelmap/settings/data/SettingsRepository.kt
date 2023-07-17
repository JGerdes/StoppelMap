package com.jonasgerdes.stoppelmap.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
    private val developerModeActive = booleanPreferencesKey("developerModeActive")

    fun getSettings() = dataStore.data.map { preferences ->
        Settings(
            themeSetting = saveValueOf(preferences[themeSettingKey], ThemeSetting.default),
            colorSchemeSetting = saveValueOf(
                preferences[colorSchemeSettingKey],
                ColorSchemeSetting.default,
            ),
            developerModeActive = preferences[developerModeActive] ?: false,
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

    suspend fun setDeveloperModeActive(active: Boolean) {
        dataStore.edit { settings ->
            settings[developerModeActive] = active
        }
    }
}

data class Settings(
    val themeSetting: ThemeSetting,
    val colorSchemeSetting: ColorSchemeSetting,
    val developerModeActive: Boolean,
)
