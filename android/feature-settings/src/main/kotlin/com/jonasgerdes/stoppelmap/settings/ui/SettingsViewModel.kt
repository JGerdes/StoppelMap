package com.jonasgerdes.stoppelmap.settings.ui

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.settings.data.DateOverride
import com.jonasgerdes.stoppelmap.settings.data.LocationOverride
import com.jonasgerdes.stoppelmap.settings.data.SettingsRepository
import com.jonasgerdes.stoppelmap.theme.settings.ColorSchemeSetting
import com.jonasgerdes.stoppelmap.theme.settings.MapColorSetting
import com.jonasgerdes.stoppelmap.theme.settings.ThemeSetting
import com.jonasgerdes.stoppelmap.theme.util.supportsDarkTheme
import com.jonasgerdes.stoppelmap.theme.util.supportsDynamicColor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val appInfo: AppInfo,
) : ViewModel() {

    val state: StateFlow<ViewState> =
        settingsRepository.getSettings()
            .map { settings ->
                Timber.d("Settings: $settings")
                ViewState(
                    appInfo = appInfo,
                    themeSettings = listOfNotNull(
                        ThemeSetting.Light,
                        ThemeSetting.Dark,
                        ThemeSetting.System.takeIf { Build.VERSION.SDK_INT.supportsDarkTheme() }
                    ).map {
                        ThemeSettingOption(
                            themeSetting = it,
                            isSelected = settings.themeSetting == it
                        )
                    },
                    colorSchemeSettings = listOfNotNull(
                        ColorSchemeSetting.Classic,
                        ColorSchemeSetting.System.takeIf { Build.VERSION.SDK_INT.supportsDynamicColor() }
                    ).map {
                        ColorSchemeSettingOption(
                            colorSchemeSetting = it,
                            isSelected = settings.colorSchemeSetting == it
                        )
                    },
                    mapColorSettings = listOfNotNull(
                        MapColorSetting.Classic,
                        MapColorSetting.AppScheme.takeIf { Build.VERSION.SDK_INT.supportsDynamicColor() }
                    ).map {
                        Option(
                            value = it,
                            isSelected = settings.mapColorSetting == it
                        )
                    },
                    developerModeSettings = if (settings.developerModeActive) {
                        DeveloperModeSettings.Active(
                            dateOverrideOptions = DateOverride.values().map {
                                Option(
                                    value = it,
                                    isSelected = it == settings.dateOverride
                                )
                            },
                            locationOverrideOptions = LocationOverride.values().map {
                                Option(
                                    value = it,
                                    isSelected = it == settings.locationOverride
                                )
                            }
                        )
                    } else DeveloperModeSettings.NotActive,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState(
                    appInfo = appInfo,
                )
            )

    fun onThemeSettingSelected(themeSetting: ThemeSetting) =
        viewModelScope.launch {
            settingsRepository.saveThemeSetting(themeSetting)
        }

    fun onColorSchemeSettingSelected(colorSchemeSetting: ColorSchemeSetting) =
        viewModelScope.launch {
            settingsRepository.saveColorSchemeSetting(colorSchemeSetting)
        }

    fun onMapColorSchemeSettingSelected(mapColorSetting: MapColorSetting) =
        viewModelScope.launch {
            settingsRepository.saveMapColorSetting(mapColorSetting)
        }

    private var versionClickCount = 0
    fun onVersionClick() {
        versionClickCount++
        if (versionClickCount > 3) {
            viewModelScope.launch {
                settingsRepository.setDeveloperModeActive(true)
            }
        }
    }

    fun onDateOverrideSelected(dateOverride: DateOverride) =
        viewModelScope.launch {
            settingsRepository.saveDateOverride(dateOverride)
        }

    fun onLocationOverrideSelected(locationOverride: LocationOverride) =
        viewModelScope.launch {
            settingsRepository.saveLocationOverride(locationOverride)
        }

    data class ViewState(
        val appInfo: AppInfo,
        val themeSettings: List<ThemeSettingOption> = emptyList(),
        val colorSchemeSettings: List<ColorSchemeSettingOption> = emptyList(),
        val mapColorSettings: List<Option<MapColorSetting>> = emptyList(),
        val developerModeSettings: DeveloperModeSettings = DeveloperModeSettings.NotActive,
    )

    data class ThemeSettingOption(
        val themeSetting: ThemeSetting,
        val isSelected: Boolean = false,
    )

    data class ColorSchemeSettingOption(
        val colorSchemeSetting: ColorSchemeSetting,
        val isSelected: Boolean = false,
    )

    sealed interface DeveloperModeSettings {
        object NotActive : DeveloperModeSettings
        data class Active(
            val dateOverrideOptions: List<Option<DateOverride>>,
            val locationOverrideOptions: List<Option<LocationOverride>>,
        ) : DeveloperModeSettings
    }

    data class Option<T>(
        val value: T,
        val isSelected: Boolean = false,
    )
}
