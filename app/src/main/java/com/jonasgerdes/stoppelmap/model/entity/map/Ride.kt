package com.jonasgerdes.stoppelmap.model.entity.map

import com.jonasgerdes.stoppelmap.model.entity.Price
import io.realm.RealmList
import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 15.06.2017
 */
open class Ride : RealmObject() {
    var isForKids: Boolean = false
    var type: String? = null
    var prices: RealmList<Price> = RealmList()
}