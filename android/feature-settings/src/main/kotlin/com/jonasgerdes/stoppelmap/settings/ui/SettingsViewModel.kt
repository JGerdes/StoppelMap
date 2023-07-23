package com.jonasgerdes.stoppelmap.settings.ui

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.base.contract.AppInfo
import com.jonasgerdes.stoppelmap.settings.data.DateOverride
import com.jonasgerdes.stoppelmap.settings.data.ImageSource
import com.jonasgerdes.stoppelmap.settings.data.Library
import com.jonasgerdes.stoppelmap.settings.data.SettingsRepository
import com.jonasgerdes.stoppelmap.theme.settings.ColorSchemeSetting
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
    private val libraries: List<Library>,
    private val imageSources: List<ImageSource>,
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
                    developerModeSettings = if (settings.developerModeActive) {
                        DeveloperModeSettings.Active(
                            dateOverrideOptions = DateOverride.values().map {
                                DateOverrideOption(
                                    dateOverride = it,
                                    isSelected = it == settings.dateOverride
                                )
                            }
                        )
                    } else DeveloperModeSettings.NotActive,
                    libraries = libraries,
                    imageSources = imageSources,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState(
                    appInfo = appInfo,
                    libraries = libraries,
                    imageSources = imageSources,
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

    data class ViewState(
        val appInfo: AppInfo,
        val themeSettings: List<ThemeSettingOption> = emptyList(),
        val colorSchemeSettings: List<ColorSchemeSettingOption> = emptyList(),
        val developerModeSettings: DeveloperModeSettings = DeveloperModeSettings.NotActive,
        val libraries: List<Library>,
        val imageSources: List<ImageSource>,
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
            val dateOverrideOptions: List<DateOverrideOption>,
        ) : DeveloperModeSettings
    }

    data class DateOverrideOption(
        val dateOverride: DateOverride,
        val isSelected: Boolean = false,
    )
}
