@file:OptIn(ExperimentalPermissionsApi::class)

package com.jonasgerdes.stoppelmap.map.components

import android.annotation.SuppressLint
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import androidx.annotation.DrawableRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.jonasgerdes.stoppelmap.map.MapDefaults
import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.ui.MapColors
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel.Camera
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel.MapState
import com.jonasgerdes.stoppelmap.shared.resources.Res
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.LocationComponentOptions
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.BackgroundLayer
import org.maplibre.android.style.layers.FillLayer
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory.backgroundColor
import org.maplibre.android.style.layers.PropertyFactory.fillColor
import org.maplibre.android.style.layers.PropertyFactory.textColor
import org.maplibre.android.style.layers.PropertyFactory.textHaloColor
import org.maplibre.android.style.layers.PropertyFactory.visibility
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import timber.log.Timber
import java.net.URI

@SuppressLint("MissingPermission")
@Composable
fun Map(
    mapState: MapState,
    mapDataFile: String,
    onCameraUpdateDispatched: () -> Unit,
    onCameraMoved: () -> Unit,
    onStallTap: (String) -> Unit,
    modifier: Modifier = Modifier,
    colors: MapColors,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val isDarkTheme = isSystemInDarkTheme()
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    Timber.d("mapColors: ${colors.toSwiftDict()}")

    val compassMargins = with(LocalDensity.current) {
        val layoutDir = LocalLayoutDirection.current
        Rect(
            /* left = */ if (layoutDir == LayoutDirection.Ltr) 0 else 16.dp.roundToPx(),
            /* top = */ WindowInsets.statusBars.getTop(this) + 16.dp.roundToPx(),
            /* right = */ if (layoutDir == LayoutDirection.Rtl) 0 else 16.dp.roundToPx(),
            /* bottom = */ 0
        )
    }
    var isCameraMoving by remember { mutableStateOf(false) }
    var cameraPosition by rememberSaveable {
        mutableStateOf(CameraPosition.DEFAULT)
    }
    val mapView = remember {
        Timber.d("new Mapview")
        MapLibre.getInstance(context)
        MapView(context).apply {
            getMapAsync { map ->
                map.uiSettings.apply {
                    setCompassMargins(
                        compassMargins.left,
                        compassMargins.top,
                        compassMargins.right,
                        compassMargins.bottom,
                    )
                    isAttributionEnabled = false
                    isLogoEnabled = false
                }
                map.setMinPitchPreference(0.0)
                map.setMaxPitchPreference(0.0)
                map.setLatLngBoundsForCameraTarget(MapDefaults.cameraBounds)
                map.setMinZoomPreference(MapDefaults.minZoom)
                map.setMaxZoomPreference(MapDefaults.maxZoom)
                map.addOnCameraIdleListener {
                    isCameraMoving = false
                    cameraPosition = map.cameraPosition
                }
                map.addOnCameraMoveStartedListener {
                    isCameraMoving = true
                    onCameraMoved()
                }
                map.setStyle(Style.Builder()
                    .fromUri("asset://${Res.assets.map.style.path}")
                    .apply {
                        sources.add(
                            GeoJsonSource(
                                id = "composite",
                                uri = URI.create(mapDataFile)
                            )
                        )
                    })

                map.addOnMapClickListener { point ->
                    val result = map.queryRenderedFeatures(
                        map.projection.toScreenLocation(point),
                        "rides",
                        "bars",
                        "restaurants",
                        "restrooms",
                        "miscs",
                        "food-stalls",
                        "candy-stalls",
                        "game-stalls",
                        "seller-stalls",
                        "expo-stalls"
                    )
                    val tappedStall = result.firstOrNull()
                    val slug = tappedStall?.getStringProperty("slug")
                    if (slug != null) {
                        onStallTap(slug)
                    }
                    true
                }
            }
        }
    }

    val zoomPadding = remember(density) { with(density) { 64.dp.roundToPx() } }
    // Map flashes white for some frames, work around that by fading it in after some time
    /*val mapAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(100)
        mapAlpha.animateTo(1f, tween(durationMillis = 300, easing = LinearEasing))
    }*/

    LaunchedEffect(Unit) {
        mapView.getMapAsync {
            it.cameraPosition = cameraPosition
        }
    }


    AndroidView(factory = { mapView }, modifier = modifier) {
        it.getMapAsync { map ->
            map.apply {
                if (!isCameraMoving) {
                    when (val camera = mapState.camera) {
                        null -> Unit
                        is Camera.FocusLocation -> {
                            map.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    camera.location,
                                    camera.zoom
                                )
                            )
                            onCameraUpdateDispatched()
                        }

                        is Camera.IncludeLocations -> {
                            map.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(
                                    LatLngBounds.fromLatLngs(camera.locations), zoomPadding
                                )
                            )
                            onCameraUpdateDispatched()
                        }
                    }
                }

                if (locationPermissionState.status.isGranted) {
                    map.getStyle { style ->
                        map.locationComponent.activateLocationComponent(
                            LocationComponentActivationOptions.builder(
                                context,
                                style
                            )
                                .locationComponentOptions(
                                    LocationComponentOptions.builder(context)
                                        .pulseEnabled(true)
                                        .build()
                                )
                                .build()
                        )
                        map.locationComponent.isLocationComponentEnabled = true
                    }
                }

                getStyle { style ->
                    style.getLayerAs<BackgroundLayer>("background")?.withProperties(
                        backgroundColor(colors.backgroundColor.toArgb())
                    )
                    style.getLayerAs<FillLayer>("streets")?.withProperties(
                        fillColor(colors.streetColor.toArgb())
                    )
                    style.getLayerAs<FillLayer>("rides")?.withProperties(
                        fillColor(colors.stallTypeRideColor.toArgb())
                    )
                    style.getLayerAs<FillLayer>("bars")?.withProperties(
                        fillColor(colors.stallTypeBarColor.toArgb())
                    )
                    style.getLayerAs<FillLayer>("restaurants")?.withProperties(
                        fillColor(colors.stallTypeRestaurantColor.toArgb())
                    )
                    style.getLayerAs<FillLayer>("restrooms")?.withProperties(
                        fillColor(colors.stallTypeRestroomColor.toArgb())
                    )
                    style.getLayerAs<FillLayer>("miscs")?.withProperties(
                        fillColor(colors.stallTypeMiscColor.toArgb())
                    )
                    style.getLayerAs<FillLayer>("fill_public_transport")?.withProperties(
                        fillColor(colors.stallTypeMiscColor.toArgb())
                    )
                    style.getLayerAs<FillLayer>("food-stalls")?.withProperties(
                        fillColor(colors.stallTypeFoodStallColor.toArgb())
                    )
                    style.getLayerAs<FillLayer>("candy-stalls")?.withProperties(
                        fillColor(colors.stallTypeCandyStallColor.toArgb())
                    )
                    style.getLayerAs<FillLayer>("game-stalls")?.withProperties(
                        fillColor(colors.stallTypeGameStallColor.toArgb())
                    )
                    style.getLayerAs<FillLayer>("seller-stalls")?.withProperties(
                        fillColor(colors.stallTypeSellerStallColor.toArgb())
                    )
                    style.getLayerAs<FillLayer>("expo-stalls")?.withProperties(
                        fillColor(colors.stallTypeExpoColor.toArgb())
                    )

                    val highlightedStalls = mapState.highlightedStalls

                    style.getLayerAs<SymbolLayer>("labels")?.withProperties(
                        textColor(colors.labelColor.toArgb()),
                        textHaloColor(colors.labelHaloColor.toArgb()),
                        visibility(if (highlightedStalls.isNullOrEmpty()) Property.VISIBLE else Property.NONE)
                    )

                    style.addMarkerIcons(context, density, colors, isDarkTheme)

                    style.getLayerAs<SymbolLayer>("highlight-labels")?.withProperties(
                        textColor(colors.labelColor.toArgb()),
                        textHaloColor(colors.labelHaloColor.toArgb()),
                        visibility(if (highlightedStalls.isNullOrEmpty()) Property.NONE else Property.VISIBLE),
                    )
                    if (highlightedStalls != null) {
                        style.getSourceAs<GeoJsonSource>("highlight-labels-source")?.apply {
                            setGeoJson(
                                FeatureCollection.fromFeatures(
                                    highlightedStalls.map { stall ->
                                        Feature.fromGeometry(
                                            Point.fromLngLat(
                                                stall.center_lng,
                                                stall.center_lat
                                            ),
                                            JsonObject().apply {
                                                add("building", JsonPrimitive(stall.type))
                                                stall.name?.let { name ->
                                                    add("name", JsonPrimitive(name))
                                                }
                                            }
                                        )
                                    }
                                ).toJson()
                            )
                        }
                    }
                }
            }
        }
    }
    MapLifecycle(mapView = mapView)
}

private fun MapColors.toSwiftDict(): String =
    mapOf(
        "backgroundColor" to "UIColor(red:${backgroundColor.red}, green:${backgroundColor.green}, blue: ${backgroundColor.blue}, alpha: 1.0)",
        "streetColor" to "UIColor(red:${streetColor.red}, green:${streetColor.green}, blue: ${streetColor.blue}, alpha: 1.0)",
        "labelColor" to "UIColor(red:${labelColor.red}, green:${labelColor.green}, blue: ${labelColor.blue}, alpha: 1.0)",
        "labelHaloColor" to "UIColor(red:${labelHaloColor.red}, green:${labelHaloColor.green}, blue: ${labelHaloColor.blue}, alpha: 1.0)",
        "stallTypeBarColor" to "UIColor(red:${stallTypeBarColor.red}, green:${stallTypeBarColor.green}, blue: ${stallTypeBarColor.blue}, alpha: 1.0)",
        "stallTypeCandyStallColor" to "UIColor(red:${stallTypeCandyStallColor.red}, green:${stallTypeCandyStallColor.green}, blue: ${stallTypeCandyStallColor.blue}, alpha: 1.0)",
        "stallTypeExpoColor" to "UIColor(red:${stallTypeExpoColor.red}, green:${stallTypeExpoColor.green}, blue: ${stallTypeExpoColor.blue}, alpha: 1.0)",
        "stallTypeFoodStallColor" to "UIColor(red:${stallTypeFoodStallColor.red}, green:${stallTypeFoodStallColor.green}, blue: ${stallTypeFoodStallColor.blue}, alpha: 1.0)",
        "stallTypeGameStallColor" to "UIColor(red:${stallTypeGameStallColor.red}, green:${stallTypeGameStallColor.green}, blue: ${stallTypeGameStallColor.blue}, alpha: 1.0)",
        "stallTypeMiscColor" to "UIColor(red:${stallTypeMiscColor.red}, green:${stallTypeMiscColor.green}, blue: ${stallTypeMiscColor.blue}, alpha: 1.0)",
        "stallTypeParkingColor" to "UIColor(red:${stallTypeParkingColor.red}, green:${stallTypeParkingColor.green}, blue: ${stallTypeParkingColor.blue}, alpha: 1.0)",
        "stallTypeRestaurantColor" to "UIColor(red:${stallTypeRestaurantColor.red}, green:${stallTypeRestaurantColor.green}, blue: ${stallTypeRestaurantColor.blue}, alpha: 1.0)",
        "stallTypeRestroomColor" to "UIColor(red:${stallTypeRestroomColor.red}, green:${stallTypeRestroomColor.green}, blue: ${stallTypeRestroomColor.blue}, alpha: 1.0)",
        "stallTypeRideColor" to "UIColor(red:${stallTypeRideColor.red}, green:${stallTypeRideColor.green}, blue: ${stallTypeRideColor.blue}, alpha: 1.0)",
        "stallTypeSellerStallColor" to "UIColor(red:${stallTypeSellerStallColor.red}, green:${stallTypeSellerStallColor.green}, blue: ${stallTypeSellerStallColor.blue / 255f}, alpha: 1.0)",
    ).entries
        .map { "\"${it.key}\": ${it.value}" }
        .joinToString(
            """,
            |
        """.trimMargin()
        ).let {
            """[
            |$it
        ]""".trimMargin()
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
            Lifecycle.Event.ON_RESUME -> this.onResume()
            Lifecycle.Event.ON_PAUSE -> this.onPause()
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
    colors: MapColors,
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
        MarkerIcon("station", R.drawable.ic_bus, colors.stallTypeMiscColor),
        MarkerIcon("platform", R.drawable.ic_train, colors.stallTypeMiscColor),
        MarkerIcon("taxi", R.drawable.ic_taxi, colors.stallTypeMiscColor),
        MarkerIcon("entrance", R.drawable.ic_entrance, colors.stallTypeMiscColor),
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
