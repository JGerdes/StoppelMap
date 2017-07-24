package com.jonasgerdes.stoppelmap.model.entity

import io.realm.RealmList
import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.06.2017
 */
open class Product : RealmObject() {
    var name: String = "" //will be set via realm anyways, rather empty than nullable
    var prices: RealmList<Price> = RealmList()
}