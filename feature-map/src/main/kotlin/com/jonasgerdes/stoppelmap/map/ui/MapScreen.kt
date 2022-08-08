package com.jonasgerdes.stoppelmap.map.ui

import android.content.ComponentCallbacks
import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.jonasgerdes.stoppelmap.map.MapDefaults
import com.mapbox.maps.CameraBoundsOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.maps.toCameraOptions
import timber.log.Timber

@Composable
fun MapScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    var cameraOptions by rememberSaveable {
        mutableStateOf(
            CameraOptions.Builder()
                .center(MapDefaults.center)
                .zoom(MapDefaults.defaultZoom)
                .build()
        )
    }

    val mapView = remember {
        MapView(context).apply {
            getMapboxMap().apply {
                loadStyleUri("asset://style.json")
                logo.enabled = false
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
            }
        }
    }
    AndroidView(factory = { mapView }) {
        it.getMapboxMap().apply {
            setCamera(cameraOptions)
            this.addOnCameraChangeListener {
                Timber.d("addOnCameraChangeListener: $cameraState")
                cameraOptions = this.cameraState.toCameraOptions()
            }
        }
    }
    MapLifecycle(mapView = mapView)
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
