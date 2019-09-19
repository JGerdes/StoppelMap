package com.jonasgerdes.stoppelmap.events.usecase

import org.threeten.bp.LocalDateTime

fun LocalDateTime.atStoppelmarktOffset() = atOffset(org.threeten.bp.ZoneOffset.ofHours(2))
