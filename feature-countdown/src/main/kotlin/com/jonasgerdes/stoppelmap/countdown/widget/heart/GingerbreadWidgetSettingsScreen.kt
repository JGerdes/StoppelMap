package com.jonasgerdes.stoppelmap.countdown.widget.heart

import android.widget.FrameLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.jonasgerdes.stoppelmap.countdown.ui.components.settings.*

@Composable
fun GingerbreadWidgetSettingsScreen(
    state: GingerbreadWidgetSettingsViewModel.ViewState,
    onShowHoursChange: (Boolean) -> Unit,
    onHueChange: (Float) -> Unit,
    onSaturationChange: (Float) -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onColorSelect: (Int) -> Unit,
    onSaveTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CheckerBoxBackground(Modifier.fillMaxSize())
        if (state is GingerbreadWidgetSettingsViewModel.ViewState.Loaded) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Top
            ) {
                GingerbreadWidgetPreview(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    settings = state.settings,
                )
                Column(Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .size(16.dp)
                            .weight(1f)
                    )
                    WidgetSettingsPager(
                        onSave = onSaveTap,
                        settingsCards = listOf(
                            { modifier ->
                                ColorSettingsCard(
                                    hue = state.colorSettings.hue,
                                    saturation = state.colorSettings.saturation,
                                    brightness = state.colorSettings.brightness,
                                    onHueChanged = onHueChange,
                                    onSaturationChanged = onSaturationChange,
                                    onBrightnessChanged = onBrightnessChange,
                                    onColorChanged = onColorSelect,
                                    selectableColors = DEFAULT_COLORS + listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.tertiary,
                                    ),
                                    modifier = modifier.fillMaxWidth(),
                                )
                            },
                            { modifier ->
                                ShowHoursSettingsCard(
                                    showHours = state.displaySettings.showHours,
                                    onShowHoursChanged = onShowHoursChange,
                                    modifier = modifier.fillMaxWidth()
                                )
                            }
                        )
                    )
                }
            }

        }
    }
}


@Composable
fun GingerbreadWidgetPreview(modifier: Modifier, settings: GingerbreadWidgetSettings) {
    val widgetProvider = remember { GingerbreadHeartWidgetProvider() }
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            FrameLayout(context)
        },
        update = {
            it.removeAllViews()
            val views = widgetProvider.initWidget(context, settings)
            it.addView(views.apply(context, it))
        }
    )
}
