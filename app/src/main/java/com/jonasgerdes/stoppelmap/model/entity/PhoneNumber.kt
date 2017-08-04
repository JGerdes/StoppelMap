package com.jonasgerdes.stoppelmap.model.entity

import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.2017
 */
open class PhoneNumber : RealmObject() {

    var name: String? = null
    var callableNumber: String? = null
    var prettifiedNumber: String? = null
}