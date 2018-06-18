package com.jonasgerdes.stoppelmap.map

import com.jonasgerdes.stoppelmap.model.map.MarkerItem
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource


private object Constants {
    const val markerLayerId = "selected-marker-layer"
    val symbolLayerIds = (1..5).map { "label $it" }.union(listOf("label parking"))
    const val sourceName = "selected-marker"
}

fun MapboxMap.removeMarkers() = setMarkers(emptyList())

fun MapboxMap.setMarkers(items: List<MarkerItem>) {

    val features = FeatureCollection.fromFeatures(items.map {
        Feature.fromGeometry(Point.fromLngLat(it.location.longitude,
                it.location.latitude)).apply {
            addStringProperty("type", it.type)
        }
    })
    val source = getSourceAs(Constants.sourceName) as GeoJsonSource?
    source?.setGeoJson(features)

    val symbolVisibility = if (items.isEmpty()) Property.VISIBLE else Property.NONE
    Constants.symbolLayerIds
            .map { getLayer(it) as? SymbolLayer }
            .forEach { it?.setProperties(visibility(symbolVisibility)) }
}