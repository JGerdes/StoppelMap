package com.jonasgerdes.stoppelmap.widget.glance

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.util.TypedValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.glance.LocalContext

fun String.renderToBitmap(
    size: Float,
    color: Int,
    typeface: Typeface,
): Bitmap {
    val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = size
        this.color = color
        this.typeface = typeface
    }
    val width = paint.measureText(this)
    val height = paint.descent() - paint.ascent()

    return Bitmap.createBitmap(
        width.toInt(),
        height.toInt(),
        Bitmap.Config.ARGB_8888
    ).also {
        Canvas(it).drawText(this, 0f, -paint.ascent(), paint)
    }
}


@Composable
fun TextUnit.toPx() =
    if (this.isSp) {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            value,
            LocalContext.current.resources.displayMetrics
        )
    } else throw IllegalArgumentException("Only sp supported")

@Composable
fun Dp.toPx() =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value,
        LocalContext.current.resources.displayMetrics
    )
