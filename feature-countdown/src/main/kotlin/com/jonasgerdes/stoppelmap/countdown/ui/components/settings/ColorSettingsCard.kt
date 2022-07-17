@file:OptIn(ExperimentalMaterial3Api::class)

package com.jonasgerdes.stoppelmap.countdown.ui.components.settings

import android.graphics.Color.colorToHSV
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import com.jonasgerdes.stoppelmap.theme.StoppelPink
import com.jonasgerdes.stoppelmap.theme.StoppelPurple
import kotlin.math.max
import android.graphics.Color.HSVToColor as hsvToColor

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
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                onColorsChanged(deriveColorsFrom(color.toArgb()))
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

            val hsv = remember(colors.first()) {
                val out = FloatArray(3)
                ColorUtils.colorToHSL(colors.first(), out)
                out
            }

            HueSlider(
                selectedHue = hsv[0],
                onHueSelected = { updatedHue ->
                    onColorsChanged(
                        deriveColorsFrom(
                            hsvToColor(
                                floatArrayOf(updatedHue, hsv[1], hsv[2])
                            )
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

        }
    }
}

@Composable
fun HueSlider(selectedHue: Float, onHueSelected: (Float) -> Unit, modifier: Modifier = Modifier) {

    var width: Int? by remember { mutableStateOf(null) }

    Box(
        modifier = modifier
            .height(16.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    width?.let {
                        onHueSelected(
                            ((offset.x - 8.dp.toPx()) / (it - 16.dp.toPx())).coerceIn(
                                0f,
                                1f
                            ) * 359
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
                            onHueSelected(
                                ((offsetX - 8.dp.toPx()) / (it - 16.dp.toPx())).coerceIn(
                                    0f,
                                    1f
                                ) * 359
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
            val handleCenterX = (selectedHue / 359) * effectiveWidth + start
            for (pixel in 0 until effectiveWidth.toInt()) {
                val hue = (pixel / effectiveWidth) * 360
                drawRect(
                    color = Color.hsv(hue = hue, saturation = 1f, value = 1f),
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
                Color.hsv(selectedHue, saturation = 1f, value = 1f),
                radius = 8.dp.toPx(),
                center = center.copy(x = handleCenterX)
            )
        }
    }
}

@Preview
@Composable
fun ColorSettingsCardPreview() {
    StoppelMapTheme {
        ColorSettingsCard(colors = listOf(), onColorsChanged = {})
    }
}

fun deriveColorsFrom(color: Int): List<Int> {
    val color1AsHsv = FloatArray(3)
    colorToHSV(color, color1AsHsv)

    val color2AsHsv = floatArrayOf(color1AsHsv[0], color1AsHsv[1], color1AsHsv[2])
    color1AsHsv[1] = max(0.1f, color1AsHsv[1] - 0.4f)
    color1AsHsv[2] = 0.75f


    color2AsHsv[1] += 0.3f
    color2AsHsv[2] = max(0.4f, color2AsHsv[2] - 0.3f)

    return listOf(
        hsvToColor(color1AsHsv),
        color,
        hsvToColor(color2AsHsv)
    )
}
