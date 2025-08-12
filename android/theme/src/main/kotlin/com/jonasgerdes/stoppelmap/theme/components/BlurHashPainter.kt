package com.jonasgerdes.stoppelmap.theme.components

import android.graphics.Bitmap
import android.graphics.Rect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import com.wolt.blurhashkt.BlurHashDecoder

@Composable
fun rememberBlurHashPainter(blurHash: String): Painter {
    return remember(blurHash) { BlurHashPainter(blurHash) }
}

private class BlurHashPainter(private val blurHash: String) : Painter() {
    override val intrinsicSize = Size.Unspecified

    private var bitmapCache: Bitmap? = null
    private var cacheSize: Size? = null

    override fun DrawScope.onDraw() {
        val halfWidth = size.width.toInt() / 8
        val halfHeight = size.height.toInt() / 8
        val bitmap = bitmapCache?.takeIf { cacheSize == size } ?: BlurHashDecoder.decode(
            blurHash = blurHash,
            width = halfWidth,
            height = halfHeight,
        )?.also {
            bitmapCache = it
            cacheSize = size
        }

        bitmap?.let {
            drawContext.canvas.nativeCanvas.drawBitmap(
                it,
                Rect(0, 0, halfWidth, halfHeight),
                Rect(0, 0, size.width.toInt(), size.height.toInt()),
                null
            )
        }
    }

}