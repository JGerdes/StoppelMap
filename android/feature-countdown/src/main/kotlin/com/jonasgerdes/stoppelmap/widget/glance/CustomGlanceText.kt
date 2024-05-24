package com.jonasgerdes.stoppelmap.widget.glance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext

@Composable
fun CustomGlanceText(
    text: String,
    color: Color = Color.Black,
    style: StoppelMapGlanceTheme.TextStyle,
    modifier: GlanceModifier = GlanceModifier
) {
    val context = LocalContext.current
    val textSize = style.size.toPx()

    val bitmap = remember(text, style) {
        text.renderToBitmap(
            size = textSize,
            color = color.toArgb(),
            typeface = context.resources.getFont(style.font)
        )
    }

    Image(provider = ImageProvider(bitmap), contentDescription = null, modifier = modifier)
}