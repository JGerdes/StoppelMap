package com.jonasgerdes.stoppelmap.countdown.widget.skyline

import android.widget.FrameLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.countdown.ui.Font
import com.jonasgerdes.stoppelmap.countdown.ui.components.settings.*
import com.jonasgerdes.stoppelmap.widget.silhouette.SilhouetteWidgetProvider

@Composable
fun WidgetSettingsScreen(
    state: SkylineWidgetSettingsViewModel.ViewState,
    onShowHoursChange: (Boolean) -> Unit,
    onHueChange: (Float) -> Unit,
    onSaturationChange: (Float) -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onColorSelect: (Int) -> Unit,
    onFontHueChange: (Float) -> Unit,
    onFontSaturationChange: (Float) -> Unit,
    onFontBrightnessChange: (Float) -> Unit,
    onFontColorSelect: (Int) -> Unit,
    onFontSelect: (Font) -> Unit,
    onSaveTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CheckerBoxBackground(Modifier.fillMaxSize())
        if (state is SkylineWidgetSettingsViewModel.ViewState.Loaded) {
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
                                ColorSettingsCard(
                                    hue = state.fontColorSettings.hue,
                                    saturation = state.fontColorSettings.saturation,
                                    brightness = state.fontColorSettings.brightness,
                                    onHueChanged = onFontHueChange,
                                    onSaturationChanged = onFontSaturationChange,
                                    onBrightnessChanged = onFontBrightnessChange,
                                    onColorChanged = onFontColorSelect,
                                    title = stringResource(R.string.widget_configuration_font_color_title),
                                    selectableColors = setOf(
                                        Color(0xfff4f4f4),
                                        Color(0xff000000),
                                        MaterialTheme.colorScheme.onPrimary,
                                        MaterialTheme.colorScheme.onSecondary,
                                        MaterialTheme.colorScheme.onTertiary,
                                    ).toList(),
                                    modifier = modifier.fillMaxWidth(),
                                )
                            },
                            { modifier ->
                                DisplaySettingsCard(
                                    showHours = state.displaySettings.showHours,
                                    onShowHoursChanged = onShowHoursChange,
                                    selectableFonts = Font.values().toList(),
                                    font = state.displaySettings.font,
                                    onFontChanged = onFontSelect,
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
fun GingerbreadWidgetPreview(modifier: Modifier, settings: SkylineWidgetSettings) {
    val widgetProvider = remember { SilhouetteWidgetProvider() }
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
