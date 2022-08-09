package com.jonasgerdes.stoppelmap.map.components

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import androidx.annotation.DrawableRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.jonasgerdes.stoppelmap.map.MapDefaults
import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel.MapState
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.layers.generated.BackgroundLayer
import com.mapbox.maps.extension.style.layers.generated.FillLayer
import com.mapbox.maps.extension.style.layers.generated.SymbolLayer
import com.mapbox.maps.extension.style.layers.getLayerAs
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.scalebar.scalebar
import timber.log.Timber

@Composable
fun MapboxMap(
    mapState: MapState,
    onCameraMove: (CameraOptions) -> Unit,
    onStallTap: (String) -> Unit,
    modifier: Modifier = Modifier,
    colors: MapBoxMapColors = MapBoxMapColors.fromMaterialTheme(),
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val isDarkTheme = isSystemInDarkTheme()
    val mapView = remember {
        MapView(context, MapInitOptions(context, styleUri = "asset://map/style.json")).apply {
            getMapboxMap().apply {
                scalebar.enabled = false
                attribution.enabled = false
                gestures.pitchEnabled = false
                setBounds(
                    CameraBoundsOptions.Builder()
                        .minZoom(MapDefaults.minZoom)
                        .maxZoom(MapDefaults.maxZoom)
                        .bounds(MapDefaults.cameraBounds)
                        .build()
                )
                addOnCameraChangeListener {
                    onCameraMove(cameraState.toCameraOptions())
                }
                getStyle()?.apply {
                    addMarkerIcons(context, density, colors, isDarkTheme)
                }
                addOnMapClickListener { point ->
                    Timber.d("addOnMapClickListener@ $point")
                    queryRenderedFeatures(
                        RenderedQueryGeometry(pixelForCoordinate(point)),
                        RenderedQueryOptions(
                            listOf(
                                "rides", "bars", "restaurants"
                            ), null
                        )
                    ) {
                        Timber.d("queryRenderedFeatures: ${it.value} - ${it.error}")
                        val tappedStall = it.value?.firstOrNull()?.feature
                        val slug = tappedStall?.getStringProperty("slug")
                        if (slug != null) {
                            onStallTap(slug)
                        }
                    }
                    true
                }

            }
        }
    }

    AndroidView(factory = { mapView }, modifier = modifier) {
        it.getMapboxMap().apply {
            when (mapState.cameraMovementSource) {
                MapState.CameraMovementSource.User -> setCamera(mapState.cameraOptions)
                MapState.CameraMovementSource.Computed -> flyTo(mapState.cameraOptions)
            }

            getStyle { style ->
                style.getLayerAs<BackgroundLayer>("background")?.apply {
                    backgroundColor(colors.backgroundColor.toArgb())
                }
                style.getLayerAs<FillLayer>("streets")?.apply {
                    fillColor(colors.streetColor.toArgb())
                }
                style.getLayerAs<FillLayer>("rides")?.apply {
                    fillColor(colors.stallTypeRideColor.toArgb())
                }
                style.getLayerAs<FillLayer>("bars")?.apply {
                    fillColor(colors.stallTypeBarColor.toArgb())
                }
                style.getLayerAs<FillLayer>("restaurants")?.apply {
                    fillColor(colors.stallTypeRestaurantColor.toArgb())
                }

                style.getLayerAs<SymbolLayer>("labels")?.apply {
                    textColor(colors.labelColor.toArgb())
                    textHaloColor(colors.labelHaloColor.toArgb())
                }
                getStyle()?.apply {
                    addMarkerIcons(context, density, colors, isDarkTheme)
                }
            }
        }
    }
    MapLifecycle(mapView = mapView)
}

data class MapBoxMapColors(
    val backgroundColor: Color,
    val streetColor: Color,
    val labelColor: Color,
    val labelHaloColor: Color,
    val stallTypeBarColor: Color,
    val stallTypeCandyStallColor: Color,
    val stallTypeExpoColor: Color,
    val stallTypeFoodStallColor: Color,
    val stallTypeGameStallColor: Color,
    val stallTypeMiscColor: Color,
    val stallTypeParkingColor: Color,
    val stallTypeRestaurantColor: Color,
    val stallTypeRestroomColor: Color,
    val stallTypeRideColor: Color,
    val stallTypeSellerStallColor: Color,
) {
    companion object {

        private fun Color.withHSL(
            hue: Float? = null,
            saturation: Float? = null,
            lightness: Float? = null,
        ) = FloatArray(3).let { hsl ->
            ColorUtils.colorToHSL(toArgb(), hsl)
            hue?.let { hsl[0] = it }
            saturation?.let { hsl[1] = it }
            lightness?.let { hsl[2] = it }
            Color(ColorUtils.HSLToColor(hsl))
        }

        private fun Color.modify(isDarkTheme: Boolean) =
            withHSL(lightness = if (isDarkTheme) 0.3f else 0.7f)

        @Composable
        fun fromMaterialTheme(
            isDarkTheme: Boolean = isSystemInDarkTheme()
        ) =
            MapBoxMapColors(
                backgroundColor = MaterialTheme.colorScheme.background,
                streetColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurface,
                labelHaloColor = MaterialTheme.colorScheme.surface,
                stallTypeBarColor = MaterialTheme.colorScheme.secondaryContainer.modify(isDarkTheme),
                stallTypeCandyStallColor = MaterialTheme.colorScheme.surfaceVariant.modify(
                    isDarkTheme
                ),
                stallTypeExpoColor = MaterialTheme.colorScheme.surfaceVariant.modify(isDarkTheme),
                stallTypeFoodStallColor = MaterialTheme.colorScheme.surfaceVariant.modify(
                    isDarkTheme
                ),
                stallTypeGameStallColor = MaterialTheme.colorScheme.surfaceVariant.modify(
                    isDarkTheme
                ),
                stallTypeMiscColor = MaterialTheme.colorScheme.surfaceVariant.modify(isDarkTheme),
                stallTypeParkingColor = MaterialTheme.colorScheme.surfaceVariant.modify(isDarkTheme),
                stallTypeRestaurantColor = MaterialTheme.colorScheme.surfaceVariant.modify(
                    isDarkTheme
                ),
                stallTypeRestroomColor = MaterialTheme.colorScheme.errorContainer.modify(isDarkTheme),
                stallTypeRideColor = MaterialTheme.colorScheme.tertiaryContainer.modify(isDarkTheme),
                stallTypeSellerStallColor = MaterialTheme.colorScheme.surfaceVariant.modify(
                    isDarkTheme
                ),
            )
    }
}

@Composable
private fun MapLifecycle(mapView: MapView) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(context, lifecycle, mapView) {
        val mapLifecycleObserver = mapView.lifecycleObserver()
        val callbacks = mapView.componentCallbacks()

        lifecycle.addObserver(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)

        onDispose {
            lifecycle.removeObserver(mapLifecycleObserver)
            context.unregisterComponentCallbacks(callbacks)
        }
    }
}

private fun MapView.lifecycleObserver(): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        event.targetState
        when (event) {
            Lifecycle.Event.ON_START -> this.onStart()
            Lifecycle.Event.ON_STOP -> this.onStop()
            Lifecycle.Event.ON_DESTROY -> this.onDestroy()
            else -> Unit
        }
    }

private fun MapView.componentCallbacks(): ComponentCallbacks =
    object : ComponentCallbacks {
        override fun onConfigurationChanged(config: Configuration) {}

        override fun onLowMemory() {
            this@componentCallbacks.onLowMemory()
        }
    }


data class MarkerIcon(
    val id: String,
    @DrawableRes val iconResource: Int,
    val tintColor: Color
)

fun Style.addMarkerIcons(
    context: Context,
    density: Density,
    colors: MapBoxMapColors,
    isDarkTheme: Boolean
) {
    val colorHSL = FloatArray(3)
    listOf(
        MarkerIcon("bar", R.drawable.ic_stall_type_bar, colors.stallTypeBarColor),
        MarkerIcon(
            "candy_stall",
            R.drawable.ic_stall_type_candy_stall,
            colors.stallTypeCandyStallColor
        ),
        MarkerIcon("expo", R.drawable.ic_stall_type_expo, colors.stallTypeExpoColor),
        MarkerIcon(
            "food_stall",
            R.drawable.ic_stall_type_food_stall,
            colors.stallTypeFoodStallColor
        ),
        MarkerIcon(
            "game_stall",
            R.drawable.ic_stall_type_game_stall,
            colors.stallTypeGameStallColor
        ),
        MarkerIcon("misc", R.drawable.ic_stall_type_info, colors.stallTypeMiscColor),
        MarkerIcon("parking", R.drawable.ic_stall_type_parking, colors.stallTypeParkingColor),
        MarkerIcon(
            "restaurant",
            R.drawable.ic_stall_type_restaurant,
            colors.stallTypeRestaurantColor
        ),
        MarkerIcon("restroom", R.drawable.ic_stall_type_restroom, colors.stallTypeRestroomColor),
        MarkerIcon("ride", R.drawable.ic_stall_type_ride, colors.stallTypeRideColor),
        MarkerIcon(
            "seller_stall",
            R.drawable.ic_stall_type_seller_stall,
            colors.stallTypeSellerStallColor
        ),
    ).forEach { markerIcon ->
        val bitmap = with(density) {
            Bitmap.createBitmap(24.dp.roundToPx(), 24.dp.roundToPx(), Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(bitmap)
        context.getDrawable(R.drawable.ic_marker_outline)!!.apply {
            setTint(colors.labelHaloColor.toArgb())
            bounds = canvas.clipBounds
            draw(canvas)
        }
        context.getDrawable(R.drawable.ic_marker_fill)!!.apply {
            ColorUtils.colorToHSL(markerIcon.tintColor.toArgb(), colorHSL)
            colorHSL[1] = 0.7f
            colorHSL[2] = if (isDarkTheme) 0.6f else 0.4f
            setTint(ColorUtils.HSLToColor(colorHSL))
            bounds = canvas.clipBounds
            draw(canvas)
        }
        context.getDrawable(markerIcon.iconResource)!!.apply {
            setTint(colors.labelHaloColor.toArgb())
            with(density) {
                bounds = canvas.clipBounds.insetBy(
                    left = 6.dp.roundToPx(),
                    top = 5.dp.roundToPx(),
                    right = 6.dp.roundToPx(),
                    bottom = 7.dp.roundToPx()
                )
            }
            draw(canvas)
        }
        addImage(markerIcon.id, bitmap)
    }
}

private fun Rect.insetBy(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) =
    Rect(this.left + left, this.top + top, this.right - right, this.bottom - bottom)
