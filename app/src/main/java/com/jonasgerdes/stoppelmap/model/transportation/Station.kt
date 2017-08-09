package com.jonasgerdes.stoppelmap.model.entity

import io.realm.RealmList
import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.2017
 */
open class Station : RealmObject() {
    var uuid: String? = null
    var name: String? = null
    var geoLocation: GeoLocation? = null
    var prices: RealmList<Price>? = null
    var days: RealmList<DepartureDay>? = null
}