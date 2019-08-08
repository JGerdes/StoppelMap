package com.jonasgerdes.stoppelmap.events.usecase

import com.jonasgerdes.stoppelmap.core.domain.DateTimeProvider
import com.jonasgerdes.stoppelmap.model.events.Event


class HasEventFinishedUseCase(
    private val dateTimeProvider: DateTimeProvider
) {

    /*operator fun invoke(event: Event) {
        event.end?.let {

        } else
    }*/
}