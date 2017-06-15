package com.jonasgerdes.stoppelmap.model.entity.map

import com.jonasgerdes.stoppelmap.model.entity.GeoLocation
import io.realm.RealmList
import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 13.06.2017
 */
open class MapEntity : RealmObject() {
    var center : GeoLocation? = null
    var bounds : RealmList<GeoLocation> = RealmList()

    class MapEntities : ArrayList<MapEntity>()
}