package com.jonasgerdes.stoppelmap.model.entity

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 13.06.2017
 */
open class GeoLocation : RealmObject() {
    @SerializedName("lon")
    var longitude: Double = 0.0
    @SerializedName("lat")
    var latitude: Double = 0.0

    val latLng get() = LatLng(latitude, longitude)
}