package com.jonasgerdes.stoppelmap.map.repository.location

import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.locationcomponent.LocationConsumer
import com.mapbox.maps.plugin.locationcomponent.LocationConsumer2
import com.mapbox.maps.plugin.locationcomponent.LocationProvider

class MapBoxLocationProvider(
    private val locationRepository: LocationRepository,
) : LocationProvider {

    private val locationConsumers = mutableSetOf<LocationConsumer>()

    override fun registerLocationConsumer(locationConsumer: LocationConsumer) {
        locationConsumers.add(locationConsumer)
    }

    override fun unRegisterLocationConsumer(locationConsumer: LocationConsumer) {
        locationConsumers.remove(locationConsumer)
    }

    suspend fun observeLocation() {
        locationRepository.getLocationUpdates()
            .collect { location ->
                val point = Point.fromLngLat(
                    location.longitude,
                    location.latitude,
                    location.altitude
                )
                val bearing = location.bearing.toDouble()
                val accuracy = location.accuracy.toDouble()
                locationConsumers.forEach { consumer ->
                    consumer.onLocationUpdated(point)
                    consumer.onBearingUpdated(bearing)
                    if (consumer is LocationConsumer2) {
                        consumer.onAccuracyRadiusUpdated(accuracy)
                    }
                }
            }
    }
}
