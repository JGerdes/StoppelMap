package com.jonasgerdes.stoppelmap.model.entity

import io.realm.RealmList
import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.2017
 */
open class Route : RealmObject() {
    var uuid: String? = null
    var name: String? = null
    var stations: RealmList<Station>? = RealmList()
    var returnStation: Station? = null

    class Routes : ArrayList<Route>()
}