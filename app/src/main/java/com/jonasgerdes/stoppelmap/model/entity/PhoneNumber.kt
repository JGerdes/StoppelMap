package com.jonasgerdes.stoppelmap.model.entity

import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 15.06.2017
 */
open class Picture : RealmObject() {

    companion object {
        val TYPE_HEADER = "header"
    }

    var filePath: String? = null
    var type: String? = null
    var source: String? = null
}