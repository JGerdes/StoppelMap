package com.jonasgerdes.stoppelmap.model.entity.map

import com.jonasgerdes.stoppelmap.model.entity.Product
import io.realm.RealmList
import io.realm.RealmObject



/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.06.2017
 */
open class CandyStall : RealmObject(){
    var products: RealmList<Product> = RealmList()

    companion object {
        val TYPE = "candy-stall"
    }
}