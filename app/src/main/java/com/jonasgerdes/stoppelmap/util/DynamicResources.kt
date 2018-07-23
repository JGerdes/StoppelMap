package com.jonasgerdes.stoppelmap.util

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import com.jonasgerdes.stoppelmap.model.map.entity.Image
import com.jonasgerdes.stoppelmap.model.map.entity.Stall
import com.jonasgerdes.stoppelmap.model.map.entity.Type


fun Context.getDrawableByName(resName: String, default: Int = 0) =
        resources.getIdentifier(resName, "drawable", packageName).let {
            if (it == 0) default else it
        }

fun Context.getColorByName(resName: String, default: Int = 0) =
        ContextCompat.getColor(this, resources.getIdentifier(resName, "color", packageName).let {
            if (it == 0) default else it
        })

fun CardView.setStallTypeBackgroundColor(type: Type, default: Int = 0) {
    setCardBackgroundColor(context.getColorForStallType(type, default))
}

fun Context.getColorForStallType(type: Type?, default: Int = 0) = getColorByName(
        "background_stall_type_${type?.fixNameForRes()}"
        , default)

fun ImageView.setStallColorTint(type: String) {
    imageTintList = ColorStateList.valueOf(context.getColorByName(
            "background_stall_type_${type.fixNameForRes()}"
    ))
}

fun Stall.getImagePath(image: Image?): String = image
        ?.let { "file:///android_asset/image/${type.type}/${it.file}" }
        ?: "file:///android_asset/image/default/${type.type}.png"

fun ImageView.setStallTypeDrawable(type: Type) {
    setImageResource(context.getDrawableByName("ic_stall_type_${type.fixNameForRes()}"))
}

fun ImageView.setDrawableFromItemOrHide(name: String) {
    setDrawableOrHide("ic_stall_item_${name.fixNameForRes()}")
}

fun ImageView.setDrawableFromTypeOrHide(name: String) {
    setDrawableOrHide("ic_stall_type_${name.fixNameForRes()}")
}


fun ImageView.setDrawableOrHide(name: String) {
    val drawable = context.getDrawableByName(name)
    if (drawable == 0) {
        visibility = View.INVISIBLE
    } else {
        visibility = View.VISIBLE
        setImageResource(drawable)
    }
}

private fun String.fixNameForRes() = replace("-", "_")
private fun Type.fixNameForRes() = type.replace("-", "_")
