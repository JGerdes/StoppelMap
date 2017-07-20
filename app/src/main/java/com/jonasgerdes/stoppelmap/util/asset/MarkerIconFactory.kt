package com.jonasgerdes.stoppelmap.util.asset

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.jonasgerdes.stoppelmap.R

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 20.06.17
 */
class MarkerIconFactory(context: Context) {

    val paint = Paint()

    init {
        with(paint) {
            color = ContextCompat.getColor(context, R.color.text_dark)
            isAntiAlias = true
            isSubpixelText = true
            textSize = 48f
        }
    }

    fun createMarker(title: String, iconResource: Int): BitmapDescriptor {

        val bitmap = Bitmap.createBitmap(480, 32, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawText(title, 0f, 32f, paint)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}