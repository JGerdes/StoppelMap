@file:OptIn(ExperimentalMaterial3Api::class)

package com.jonasgerdes.stoppelmap.countdown.ui.components.settings

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.theme.StoppelPink
import com.jonasgerdes.stoppelmap.theme.StoppelPurple
import timber.log.Timber

private val DEFAULT_COLORS = listOf(
    Color(0xff7d56c2),
    StoppelPurple,
    Color(0xff2196f3),
    Color(0xff279056),
    Color(0xfff57f17),
    StoppelPink,
)

@Composable
fun ColorSettingsCard(
    hue: Float,
    saturation: Float,
    brightness: Float,
    onHueChanged: (Float) -> Unit,
    onSaturationChanged: (Float) -> Unit,
    onBrightnessChanged: (Float) -> Unit,
    onColorChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Timber.d("h: $hue, s:$saturation, b: $brightness")
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.widget_configuration_color_title),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.size(8.dp))
            Row {
                DEFAULT_COLORS.forEach { color ->
                    Surface(
                        shape = RoundedCornerShape(2.dp),
                        color = color,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                onColorChanged(color.toArgb())
                            }
                    ) {}
                    Spacer(modifier = Modifier.size(4.dp))
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = stringResource(R.string.widget_configuration_color_section_modify),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(8.dp))

            HueSlider(
                selectedHue = hue,
                onHueChanged = { updatedHue ->
                    onHueChanged(updatedHue)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(16.dp))
            SaturationSlider(
                selectedSaturation = saturation,
                onSaturationChanged = { updatedSaturation ->
                    onSaturationChanged(updatedSaturation)
                },
                selectedHue = hue,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(16.dp))
            BrightnessSlider(
                selectedBrightness = brightness,
                onBrightnessChanged = { updatedBrightness ->
                    onBrightnessChanged(updatedBrightness)
                },
                selectedHue = hue,
                selectedSaturation = saturation,
                modifier = Modifier
                    .fillMaxWidth()
            )

        }
    }
}

@Composable
fun HueSlider(selectedHue: Float, onHueChanged: (Float) -> Unit, modifier: Modifier = Modifier) {
    ColorSlider(
        value = selectedHue / 360,
        onValueChanged = {
            onHueChanged(it * 360)
        },
        mapValueToColor = { Color.hsv(hue = it * 360, saturation = 1f, value = 1f) },
        modifier = modifier
    )
}

@Composable
fun SaturationSlider(
    selectedSaturation: Float,
    onSaturationChanged: (Float) -> Unit,
    selectedHue: Float,
    modifier: Modifier = Modifier
) {
    ColorSlider(
        value = selectedSaturation,
        onValueChanged = onSaturationChanged,
        mapValueToColor = { Color.hsv(hue = selectedHue, saturation = it, value = 1f) },
        modifier = modifier
    )
}

@Composable
fun BrightnessSlider(
    selectedBrightness: Float,
    onBrightnessChanged: (Float) -> Unit,
    selectedHue: Float,
    selectedSaturation: Float,
    modifier: Modifier = Modifier
) {
    ColorSlider(
        value = selectedBrightness,
        onValueChanged = onBrightnessChanged,
        mapValueToColor = {
            Color.hsv(
                hue = selectedHue,
                saturation = selectedSaturation,
                value = it
            )
        },
        modifier = modifier
    )
}

@Composable
fun ColorSlider(
    value: Float,
    onValueChanged: (Float) -> Unit,
    mapValueToColor: (Float) -> Color,
    modifier: Modifier = Modifier
) {

    var width: Int? by remember { mutableStateOf(null) }

    Box(
        modifier = modifier
            .height(16.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    width?.let {
                        onValueChanged(
                            ((offset.x - 8.dp.toPx()) / (it - 16.dp.toPx())).coerceIn(
                                0f,
                                1f
                            )
                        )
                    }
                }
            }
            .pointerInput(Unit) {
                var offsetX = 0f
                detectHorizontalDragGestures(
                    onDragStart = {
                        offsetX = it.x
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount
                        width?.let {
                            onValueChanged(
                                ((offsetX - 8.dp.toPx()) / (it - 16.dp.toPx())).coerceIn(
                                    0f,
                                    1f
                                )
                            )
                        }
                    }
                )
            }
            .onGloballyPositioned { coordinates ->
                width = coordinates.size.width
            }
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                // TODO: Fix work around for BlendMode.Clear
                .graphicsLayer(alpha = 0.99f)
        ) {
            val effectiveWidth = size.width - 16.dp.toPx()
            val start = 8.dp.toPx()
            val handleCenterX = value * effectiveWidth + start
            for (pixel in 0 until effectiveWidth.toInt()) {
                val valueForPixel = pixel / effectiveWidth
                drawRect(
                    color = mapValueToColor(valueForPixel),
                    topLeft = Offset(x = start + pixel.toFloat(), y = 6.dp.toPx()),
                    size = Size(width = 1f, height = 4.dp.toPx())
                )
            }
            drawCircle(
                Color.White,
                radius = 10.dp.toPx(),
                center = center.copy(x = handleCenterX),
                blendMode = BlendMode.Clear
            )
            drawCircle(
                mapValueToColor(value),
                radius = 8.dp.toPx(),
                center = center.copy(x = handleCenterX)
            )
        }
    }
}
