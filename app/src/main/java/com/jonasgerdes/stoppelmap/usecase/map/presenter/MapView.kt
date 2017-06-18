package com.jonasgerdes.stoppelmap.usecase.map.presenter

import com.google.android.gms.maps.model.LatLng
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapBounds

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
interface MapView {
    fun setMapBounds(bounds:MapBounds)
    fun setMapCamera(center:LatLng, zoom:Float)
}