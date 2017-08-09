package com.jonasgerdes.stoppelmap.model.entity

import io.realm.RealmObject
import java.util.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.2017
 */
open class Departure : RealmObject() {
    var comment: String? = null
    var time: Date? = null

    fun getDay(): Int {
        val date = Calendar.getInstance()
        date.time = time
        return DepartureDay.getDayFromCalendar(date)
    }
}