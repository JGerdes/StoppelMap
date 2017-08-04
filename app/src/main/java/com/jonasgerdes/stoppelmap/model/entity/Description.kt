package com.jonasgerdes.stoppelmap.model.entity

import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 05.08.2017
 */
open class Description : RealmObject() {
    var description: String? = null
    var source: String? = null
}