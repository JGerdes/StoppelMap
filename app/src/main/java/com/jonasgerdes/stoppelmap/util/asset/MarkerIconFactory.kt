package com.jonasgerdes.stoppelmap.util.asset

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.jonasgerdes.stoppelmap.R


/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 20.06.17
 */
class MarkerIconFactory(context: Context) {

    val textFillPaint: Paint = Paint()
    val textOutlinePaint: Paint
    val padding: Int

    init {
        val resources = context.resources

        with(textFillPaint) {
            isAntiAlias = true
            isSubpixelText = true
            textSize = resources.getDimensionPixelSize(R.dimen.map_marker_text_size).toFloat()
            color = ContextCompat.getColor(context, R.color.text_dark)
            style = Paint.Style.FILL
        }

        textOutlinePaint = Paint(textFillPaint)
        with(textOutlinePaint) {
            color = ContextCompat.getColor(context, R.color.text_light)
            alpha = 192
            style = Paint.Style.STROKE
            strokeWidth = resources.getDimensionPixelSize(R.dimen.map_marker_stroke_width).toFloat()
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

        padding = resources.getDimensionPixelSize(R.dimen.map_marker_padding)

    }

    fun createMarker(title: String, iconResource: Int): BitmapDescriptor {
        val textBounds = Rect()
        textOutlinePaint.getTextBounds(title, 0, title.length, textBounds)

        val bitmap = Bitmap.createBitmap(
                textBounds.width() + padding * 2,
                textBounds.height() + padding * 2,
                Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        val y = canvas.clipBounds.height() / 2f + textBounds.height() / 2f - textBounds.bottom

        //todo: remove when finished implementing marker drawing, only for debugging
        val debugPaint = Paint()
        debugPaint.color = Color.RED
        canvas.drawRect(0f, 0f, canvas.clipBounds.width().toFloat(),
                canvas.clipBounds.height().toFloat(), debugPaint)

        canvas.drawText(title, padding.toFloat(), y, textOutlinePaint)
        canvas.drawText(title, padding.toFloat(), y, textFillPaint)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}