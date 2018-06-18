package com.jonasgerdes.stoppelmap.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.drawable.DrawableCompat
import com.mapbox.mapboxsdk.annotations.IconFactory


fun Context.getDrawableAsBitmap(@DrawableRes id: Int, @ColorInt colorInt: Int = -1, size: Int = -1)
        : Bitmap {
    val vectorDrawable = ResourcesCompat.getDrawable(resources, id, theme)!!
    val width = if (size == -1) vectorDrawable.intrinsicWidth else size
    val height = if (size == -1) vectorDrawable.intrinsicHeight else size
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, width, height)
    if (colorInt != -1) {
        DrawableCompat.setTint(vectorDrawable, colorInt)
    }
    vectorDrawable.draw(canvas)
    return bitmap
}

fun Context.getMapBoxIcon(@DrawableRes id: Int,
                          @ColorInt colorInt: Int = -1,
                          size: Int = -1) =
        IconFactory.getInstance(this).fromBitmap(getDrawableAsBitmap(id, colorInt, size))