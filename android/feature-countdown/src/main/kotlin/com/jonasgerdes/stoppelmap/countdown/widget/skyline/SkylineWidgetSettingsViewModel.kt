package com.jonasgerdes.stoppelmap.countdown.widget.skyline

import androidx.core.graphics.ColorUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.countdown.repository.WidgetSettingsRepository
import com.jonasgerdes.stoppelmap.countdown.ui.Font
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class SkylineWidgetSettingsViewModel(
    private val widgetSettingsRepository: WidgetSettingsRepository<SkylineWidgetSettings>,
) : ViewModel() {

    private var mutableState = MutableStateFlow<ViewState>(ViewState.Loading)

    val state: StateFlow<ViewState> = mutableState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState.Loading
        )

    fun onAppWidgetIdChanged(appWidgetId: Int) {
        if (appWidgetId == (mutableState.value as? ViewState.Loaded)?.appWidgetId) {
            return
        }
        val settings = widgetSettingsRepository.loadSettings(appWidgetId)
        val colorHsl = FloatArray(3).apply {
            ColorUtils.colorToHSL(settings.color, this)
        }
        val fontColorHsl = FloatArray(3).apply {
            ColorUtils.colorToHSL(settings.fontColor, this)
        }
        mutableState.value = ViewState.Loaded(
            appWidgetId = appWidgetId,
            colorSettings = ColorSettings(
                hue = colorHsl[0],
                saturation = colorHsl[1],
                brightness = colorHsl[2]
            ),
            fontColorSettings = ColorSettings(
                hue = fontColorHsl[0],
                saturation = fontColorHsl[1],
                brightness = fontColorHsl[2]
            ),
            displaySettings = DisplaySettings(
                showHours = settings.showHours,
                font = settings.font
            ),
            settings = SkylineWidgetSettings(-1)
        ).withUpdatedSettings()
    }

    fun onShowHoursChanged(showHours: Boolean) = updateLoadedState {
        copy(
            displaySettings = displaySettings.copy(
                showHours = showHours
            )
        )
    }

    fun onFontChanged(font: Font) = updateLoadedState {
        copy(
            displaySettings = displaySettings.copy(
                font = font
            )
        )
    }

    fun onColorSelected(color: Int) = updateLoadedState {
        val hsl = FloatArray(3).apply {
            ColorUtils.colorToHSL(color, this)
        }
        copy(
            colorSettings = ColorSettings(
                hue = hsl[0],
                saturation = hsl[1],
                brightness = hsl[2]
            )
        )
    }

    fun onHueChanged(hue: Float) = updateLoadedState {
        copy(
            colorSettings = colorSettings.copy(
                hue = hue
            )
        )
    }

    fun onSaturationChanged(saturation: Float) = updateLoadedState {
        copy(
            colorSettings = colorSettings.copy(
                saturation = saturation
            )
        )
    }

    fun onBrightnessChanged(brightness: Float) = updateLoadedState {
        copy(
            colorSettings = colorSettings.copy(
                brightness = brightness
            )
        )
    }

    fun onFontColorSelected(color: Int) = updateLoadedState {
        val hsl = FloatArray(3).apply {
            ColorUtils.colorToHSL(color, this)
        }
        copy(
            fontColorSettings = ColorSettings(
                hue = hsl[0],
                saturation = hsl[1],
                brightness = hsl[2]
            )
        )
    }

    fun onFontHueChanged(hue: Float) = updateLoadedState {
        copy(
            fontColorSettings = colorSettings.copy(
                hue = hue
            )
        )
    }

    fun onFontSaturationChanged(saturation: Float) = updateLoadedState {
        copy(
            fontColorSettings = colorSettings.copy(
                saturation = saturation
            )
        )
    }

    fun onFontBrightnessChanged(brightness: Float) = updateLoadedState {
        copy(
            fontColorSettings = colorSettings.copy(
                brightness = brightness
            )
        )
    }

    fun onSaveSettingsTapped() {
        val viewState = mutableState.value as? ViewState.Loaded ?: return
        widgetSettingsRepository.saveSettings(viewState.settings)
        mutableState.value = ViewState.Done(viewState.settings)
    }

    private fun updateLoadedState(updateBlock: ViewState.Loaded.() -> ViewState.Loaded) {
        val viewState = mutableState.value as? ViewState.Loaded ?: return
        val updatedViewState = viewState.updateBlock()
        mutableState.value = updatedViewState.withUpdatedSettings()
        Timber.d("new state: ${mutableState.value}")
    }

    private fun ViewState.Loaded.withUpdatedSettings() = copy(
        settings = SkylineWidgetSettings(
            appWidgetId = appWidgetId,
            showHours = displaySettings.showHours,
            color = ColorUtils.HSLToColor(
                floatArrayOf(
                    colorSettings.hue,
                    colorSettings.saturation,
                    colorSettings.brightness
                )
            ),
            fontColor = ColorUtils.HSLToColor(
                floatArrayOf(
                    fontColorSettings.hue,
                    fontColorSettings.saturation,
                    fontColorSettings.brightness
                )
            ),
            font = displaySettings.font
        )
    )

    sealed class ViewState {
        object Loading : ViewState()
        data class Loaded(
            val appWidgetId: Int,
            val colorSettings: ColorSettings,
            val fontColorSettings: ColorSettings,
            val displaySettings: DisplaySettings,
            val settings: SkylineWidgetSettings
        ) : ViewState()

        data class Done(val settings: SkylineWidgetSettings) : ViewState()
    }

    data class DisplaySettings(
        val showHours: Boolean,
        val font: Font,
    )

    data class ColorSettings(
        val hue: Float,
        val saturation: Float,
        val brightness: Float
    )
}
