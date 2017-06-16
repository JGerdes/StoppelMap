package com.jonasgerdes.stoppelmap.model.entity.map

import com.jonasgerdes.stoppelmap.model.entity.Price
import io.realm.RealmList
import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 15.06.2017
 */
open class Restroom : RealmObject() {
    var forMen: Boolean = false
    var forWomen: Boolean = false
    var forDisabled: Boolean = false

    var prices: RealmList<Price> = RealmList()
}