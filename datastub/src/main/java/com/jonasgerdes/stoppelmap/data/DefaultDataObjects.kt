package com.jonasgerdes.stoppelmap.data

import com.jonasgerdes.stoppelmap.model.events.Event
import com.jonasgerdes.stoppelmap.model.map.Stall
import com.jonasgerdes.stoppelmap.model.map.Type
import org.threeten.bp.OffsetDateTime


val DEFAULT_STALL = Stall(
    slug = "",
    type = Type.MISC,
    centerLat = 0.0,
    centerLng = 0.0,
    minLat = 0.0,
    maxLat = 0.0,
    minLng = 0.0,
    maxLng = 0.0
)

val DEFAULT_EVENT = Event(
    slug = "",
    name = "",
    location = null,
    description = null,
    start = OffsetDateTime.MIN,
    end = null
)