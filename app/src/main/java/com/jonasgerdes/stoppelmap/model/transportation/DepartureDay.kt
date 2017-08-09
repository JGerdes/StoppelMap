package com.jonasgerdes.stoppelmap.model.entity

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*


/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.2017
 */
open class DepartureDay : RealmObject() {
    var comment: String? = null
    var depatures: RealmList<Departure> = RealmList()

    companion object {
        val DAY_INVALID = -1
        val DAY_THURSDAY = 0
        val DAY_FRIDAY = 1
        val DAY_SATURDAY = 2
        val DAY_SUNDAY = 3
        val DAY_MONDAY = 4
        val DAY_TUESDAY = 5
        //Hour next day starts (ex: 6 means if it's 4am it' still the day before)
        private val FIRST_HOUR_OF_NEW_DAY = 6
        private val FIRST_DAY = 10
        private val LAST_DAY = 15

        fun getDayFromCalendar(date: Calendar): Int {
            if (date.get(Calendar.MONTH) == Calendar.AUGUST
                    && date.get(Calendar.DAY_OF_MONTH) >= FIRST_DAY
                    && date.get(Calendar.DAY_OF_MONTH) <= LAST_DAY) {
                var dayOfMonth = date.get(Calendar.DAY_OF_MONTH)


                if (date.get(Calendar.HOUR_OF_DAY) < FIRST_HOUR_OF_NEW_DAY) {
                    dayOfMonth -= 1
                }

                when (dayOfMonth) {
                    11 -> return DAY_THURSDAY
                    12 -> return DAY_FRIDAY
                    13 -> return DAY_SATURDAY
                    14 -> return DAY_SUNDAY
                    15 -> return DAY_MONDAY
                    16 -> return DAY_TUESDAY
                }
            }
            return DAY_INVALID
        }
    }
}