package com.jonasgerdes.stoppelmap.model.events

import io.realm.RealmList
import io.realm.RealmObject



/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 10.08.2017
 */
open class Events : RealmObject() {
    var events: RealmList<Event>? = RealmList()
}