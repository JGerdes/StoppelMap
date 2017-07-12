package com.jonasgerdes.stoppelmap.model.entity.map

import com.jonasgerdes.stoppelmap.model.entity.Product
import io.realm.RealmList
import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 15.06.2017
 */
open class Bar : RealmObject() {
    var isTent: Boolean = false
    var drinks: RealmList<Product> = RealmList()

    companion object {
        val TYPE = "bar"
    }
}