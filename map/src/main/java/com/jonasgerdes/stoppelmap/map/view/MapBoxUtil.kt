package com.jonasgerdes.stoppelmap.map.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import com.jonasgerdes.androidutil.dp
import com.jonasgerdes.androidutil.getColorByName
import com.jonasgerdes.androidutil.insetBy
import com.jonasgerdes.stoppelmap.map.R
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style

class MarkerIcon(val name: String, val icon: Int)

fun loadImages(context: Context, style:Style) {
    listOf(
        MarkerIcon("bar", R.drawable.ic_stall_type_bar),
        MarkerIcon("candy_stall", R.drawable.ic_stall_type_candy_stall),
        MarkerIcon("expo", R.drawable.ic_stall_type_expo),
        MarkerIcon("food_stall", R.drawable.ic_stall_type_food_stall),
        MarkerIcon("game_stall", R.drawable.ic_stall_type_game_stall),
        MarkerIcon("misc", R.drawable.ic_stall_type_info),
        MarkerIcon("parking", R.drawable.ic_stall_type_parking),
        MarkerIcon("restaurant", R.drawable.ic_stall_type_building),
        MarkerIcon("restroom", R.drawable.ic_stall_type_restroom),
        MarkerIcon("ride", R.drawable.ic_stall_type_ride),
        MarkerIcon("seller_stall", R.drawable.ic_stall_type_seller_stall)
    ).forEach {markerIcon ->
        val color = context.getColorByName("marker_type_${markerIcon.name}", Color.RED)
        val bitmap = Bitmap.createBitmap(24.dp, 24.dp, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        context.getDrawable(R.drawable.ic_marker_outline).apply {
            bounds = canvas.clipBounds
            draw(canvas)
        }
        context.getDrawable(R.drawable.ic_marker_fill).apply {
            setTint(color)
            bounds = canvas.clipBounds
            draw(canvas)
        }
        context.getDrawable(markerIcon.icon).apply {
            setTint(Color.WHITE)
            bounds = canvas.clipBounds.insetBy(
                left = 5.dp,
                top = 4.dp,
                right = 5.dp,
                bottom = 6.dp
            )
            draw(canvas)
        }
        style.addImage(markerIcon.name, bitmap)
    }
}

fun initMapCamera(it: MapboxMap) {
    it.setLatLngBoundsForCameraTarget(Settings.cameraBounds)
    it.setMinZoomPreference(Settings.minZoom)
    it.setMaxZoomPreference(Settings.maxZoom)
    it.moveCamera(
        CameraUpdateFactory.newLatLngZoom(
            Settings.center,
            Settings.defaultZoom
        ))
}

fun initMapUi(it: MapboxMap) {
    it.uiSettings.isTiltGesturesEnabled = false
    it.uiSettings.isAttributionEnabled = false
    it.uiSettings.isLogoEnabled = false
    it.uiSettings.setCompassMargins(16.dp, (24 + 16).dp, 16.dp, 16.dp)
}