package com.jonasgerdes.stoppelmap.countdown.widget.heart

import androidx.core.graphics.ColorUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.countdown.repository.WidgetSettingsRepository
import com.jonasgerdes.stoppelmap.countdown.usecase.CalculateGingerbreadHeartColorsFromHSLUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class GingerbreadWidgetSettingsViewModel(
    private val widgetSettingsRepository: WidgetSettingsRepository<GingerbreadWidgetSettings>,
    private val calculateGingerbreadHeartColorsFromHSL: CalculateGingerbreadHeartColorsFromHSLUseCase
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
        val hsl = FloatArray(3).apply {
            ColorUtils.colorToHSL(settings.color2, this)
        }
        mutableState.value = ViewState.Loaded(
            appWidgetId = appWidgetId,
            colorSettings = ColorSettings(
                hue = hsl[0],
                saturation = hsl[1],
                brightness = hsl[2]
            ),
            displaySettings = DisplaySettings(showHours = settings.showHours),
            settings = GingerbreadWidgetSettings(-1)
        ).withUpdatedSettings()
    }

    fun onShowHoursChanged(showHours: Boolean) = updateLoadedState {
        copy(
            displaySettings = displaySettings.copy(
                showHours = showHours
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

    fun onSaveSettingsTapped() {
        val viewState = mutableState.value as? ViewState.Loaded ?: return
        widgetSettingsRepository.saveSettings(viewState.settings)
        mutableState.value = ViewState.Done(viewState.settings)
    }

    private fun updateLoadedState(updateBlock: ViewState.Loaded.() -> ViewState.Loaded) {
        val viewState = mutableState.value as? ViewState.Loaded ?: return
        val updatedViewState = viewState.updateBlock()
        mutableState.value = updatedViewState.withUpdatedSettings()
    }

    private fun ViewState.Loaded.withUpdatedSettings() = copy(
        settings = calculateGingerbreadHeartColorsFromHSL(
            hue = colorSettings.hue,
            saturation = colorSettings.saturation,
            brightness = colorSettings.brightness
        ).let { colors ->
            GingerbreadWidgetSettings(
                appWidgetId = appWidgetId,
                showHours = displaySettings.showHours,
                color1 = colors.color1,
                color2 = colors.color2,
                color3 = colors.color3,
            )
        }
    )

    sealed class ViewState {
        object Loading : ViewState()
        data class Loaded(
            val appWidgetId: Int,
            val colorSettings: ColorSettings,
            val displaySettings: DisplaySettings,
            val settings: GingerbreadWidgetSettings
        ) : ViewState()

        data class Done(val settings: GingerbreadWidgetSettings) : ViewState()
    }

    data class DisplaySettings(
        val showHours: Boolean
    )

    data class ColorSettings(
        val hue: Float,
        val saturation: Float,
        val brightness: Float
    )
}
