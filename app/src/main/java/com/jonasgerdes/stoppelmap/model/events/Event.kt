package com.jonasgerdes.stoppelmap.model.events

import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.model.realm_wrapper.RealmString
import io.realm.RealmList
import io.realm.RealmObject
import java.util.*



/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 10.08.2017
 */
open class Event : RealmObject() {

    var slug: String? = null
    var name: String? = null
    var type: Int = 0

    var day: Int = 0
    var start: Date? = null
    var end: Date? = null

    var locationSlug: String? = null
    var mapEntity: MapEntity? = null

    var description: String? = null
    var facebookUrl: String? = null
    var artists: RealmList<RealmString>? = RealmList()
    var music: RealmList<RealmString>? = RealmList()

    class Events : ArrayList<Event>()
}