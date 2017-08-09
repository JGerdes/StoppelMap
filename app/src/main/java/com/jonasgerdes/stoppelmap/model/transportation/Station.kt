package com.jonasgerdes.stoppelmap.model.entity

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.2017
 */
open class Station : RealmObject() {
    var uuid: String? = null
    var name: String? = null
    var geoLocation: GeoLocation? = null
    var prices: RealmList<Price>? = null
    var days: RealmList<DepartureDay>? = null

    fun getNextDepature(date: Calendar): Departure? {
        val departureTime = Calendar.getInstance()
        for (day in days!!) {
            for (departure in day.departures) {
                departureTime.time = departure.time
                if (departureTime.after(date)) {
                    return departure
                }
            }
        }
        return null
    }


    fun getNextDepatures(date: Calendar, count: Int): List<Departure> {
        val nextDepartures = ArrayList<Departure>(count)
        val departureTime = Calendar.getInstance()
        for (day in days!!) {
            var addNext = false
            for (departure in day.departures) {
                departureTime.time = departure.time
                if (departureTime.after(date)) {
                    addNext = true
                }
                if (addNext) {
                    nextDepartures.add(departure)
                }
                if (nextDepartures.size == count) {
                    return nextDepartures
                }
            }
        }
        return nextDepartures
    }
}