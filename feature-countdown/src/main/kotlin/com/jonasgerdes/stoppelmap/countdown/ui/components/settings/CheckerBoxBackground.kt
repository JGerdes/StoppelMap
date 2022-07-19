package com.jonasgerdes.stoppelmap.countdown.ui.components.settings

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.jonasgerdes.stoppelmap.countdown.R

@Composable
fun CheckerBoxBackground(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Canvas(
        modifier = modifier
    ) {
        val pattern =
            context.getBitmapFromVectorDrawable(R.drawable.checkerbox_tile).asImageBitmap()

        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            shader = ImageShader(pattern, TileMode.Repeated, TileMode.Repeated)
        }

        drawIntoCanvas {
            it.nativeCanvas.drawPaint(paint)
        }
        paint.reset()
    }
}

private fun Context.getBitmapFromVectorDrawable(drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(this, drawableId)!!
    val bitmap: Bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
