package com.jonasgerdes.stoppelmap.util.asset

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.util.asset.Assets.NONE


/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 20.06.17
 */
class MarkerIconFactory(val context: Context) {

    private val textFillPaint: Paint = Paint()
    private val textOutlinePaint: Paint
    private val padding: Int
    private val iconSize: Int
    private val iconColor: Int

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
            style = Paint.Style.STROKE
            strokeWidth = resources.getDimensionPixelSize(R.dimen.map_marker_stroke_width).toFloat()
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

        padding = resources.getDimensionPixelSize(R.dimen.map_marker_padding)
        iconSize = context.resources.getDimensionPixelSize(R.dimen.map_marker_icon_size)
        iconColor = ContextCompat.getColor(context, R.color.map_marker_icon)

    }

    fun createMarker(title: String, iconResource: Int): BitmapDescriptor {
        var icon: Drawable? = null
        if (iconResource != NONE) {
            icon = ContextCompat.getDrawable(context, iconResource)
        }

        val textBounds = Rect()
        textOutlinePaint.getTextBounds(title, 0, title.length, textBounds)

        var markerWidth = textBounds.width() + padding * 2
        var textX = padding.toFloat()

        if (icon != null) {
            icon.setBounds(0, 0, iconSize, iconSize)
            markerWidth += icon.bounds.width()
            textX += icon.bounds.width()
        }

        val bitmap = Bitmap.createBitmap(
                markerWidth,
                textBounds.height() + padding * 2,
                Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        val textY = canvas.clipBounds.height() / 2f + textBounds.height() / 2f - textBounds.bottom

        canvas.drawText(title, textX, textY, textOutlinePaint)
        canvas.drawText(title, textX, textY, textFillPaint)

        if (icon != null) {
            icon.mutate().setColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
            icon.draw(canvas)
        }

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}