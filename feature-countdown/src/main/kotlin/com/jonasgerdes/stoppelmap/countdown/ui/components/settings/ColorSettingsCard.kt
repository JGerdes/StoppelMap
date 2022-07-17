@file:OptIn(ExperimentalMaterial3Api::class)

package com.jonasgerdes.stoppelmap.countdown.ui.components.settings

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import com.jonasgerdes.stoppelmap.theme.StoppelPink
import com.jonasgerdes.stoppelmap.theme.StoppelPurple

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
    colors: List<Int>,
    onColorsChanged: (List<Int>) -> Unit,
    modifier: Modifier = Modifier
) {
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
                        modifier = Modifier.size(32.dp)
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

            val hsv = remember(colors.first()) {
                val out = FloatArray(3)
                ColorUtils.colorToHSL(colors.first(), out)
                out
            }

            var hue by remember { mutableStateOf(hsv[0]) }
            HueSlider(
                selectedHue = hue,
                onHueSelected = { hue = it },
                modifier = Modifier
                    .fillMaxWidth()
            )

        }
    }
}

@Composable
fun HueSlider(selectedHue: Float, onHueSelected: (Float) -> Unit, modifier: Modifier = Modifier) {

    var offsetX by remember { mutableStateOf(0f) }
    var width: Int? by remember { mutableStateOf(null) }

    Canvas(
        modifier = modifier
            .height(16.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    offsetX = it.x
                    width?.let {
                        onHueSelected((offsetX / it.toFloat()) * 360)
                    }
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        offsetX = it.x
                        width?.let {
                            onHueSelected((offsetX / it.toFloat()) * 360)
                        }
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount
                        width?.let {
                            onHueSelected((offsetX / it.toFloat()) * 360)
                        }
                    }
                )
            }
            .onGloballyPositioned { coordinates ->
                width = coordinates.size.width
                offsetX = (selectedHue / 360f) * coordinates.size.width
                onHueSelected((offsetX / coordinates.size.width.toFloat()) * 360)
            }
            // TODO: Fix work around for BlendMode.Clear
            .graphicsLayer(alpha = 0.99f)
    ) {
        for (pixel in 0 until size.width.toInt()) {
            val hue = (pixel / size.width) * 360
            drawRect(
                color = Color.hsv(hue = hue.toFloat(), saturation = 1f, value = 1f),
                topLeft = Offset(x = pixel.toFloat(), y = 6.dp.toPx()),
                size = Size(width = 1f, height = 4.dp.toPx())
            )
        }
        drawCircle(
            Color.White,
            radius = 10.dp.toPx(),
            center = center.copy(x = offsetX),
            blendMode = BlendMode.Clear
        )
        drawCircle(
            Color.hsv(selectedHue, saturation = 1f, value = 1f),
            radius = 8.dp.toPx(),
            center = center.copy(x = offsetX)
        )
    }
}

@Preview
@Composable
fun ColorSettingsCardPreview() {
    StoppelMapTheme {
        ColorSettingsCard(colors = listOf(), onColorsChanged = {})
    }
}
