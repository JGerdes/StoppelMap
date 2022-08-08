package com.jonasgerdes.stoppelmap.map.ui

import android.content.ComponentCallbacks
import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.jonasgerdes.stoppelmap.map.MapDefaults
import com.mapbox.maps.CameraBoundsOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.layers.generated.BackgroundLayer
import com.mapbox.maps.extension.style.layers.generated.FillLayer
import com.mapbox.maps.extension.style.layers.getLayerAs
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.maps.toCameraOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
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

    val mutableStateFlow = MutableStateFlow<CameraOptions>(
        CameraOptions.Builder()
            .center(MapDefaults.center)
            .zoom(MapDefaults.defaultZoom)
            .build()
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            mutableStateFlow.debounce(300).collectLatest { cameraOptions = it }
        }
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
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
    val streetColor = MaterialTheme.colorScheme.surfaceVariant.toArgb()
    AndroidView(factory = { mapView }, modifier = modifier) {
        it.getMapboxMap().apply {
            setCamera(cameraOptions)
            addOnCameraChangeListener {
                Timber.d("addOnCameraChangeListener: $cameraState")
                //cameraOptions = cameraState.toCameraOptions()
                mutableStateFlow.value = cameraState.toCameraOptions()
            }

            getStyle { style ->
                style.getLayerAs<BackgroundLayer>("background")?.apply {
                    backgroundColor(backgroundColor)
                }
                style.getLayerAs<FillLayer>("streets")?.apply {
                    fillColor(streetColor)
                }
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
