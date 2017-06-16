package com.jonasgerdes.stoppelmap.model.entity

import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 15.06.2017
 */
open class Price : RealmObject() {
    var type: String? = null
    var price: Int = 0
}