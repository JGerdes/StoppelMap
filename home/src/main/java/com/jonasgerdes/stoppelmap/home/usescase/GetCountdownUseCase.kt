package com.jonasgerdes.stoppelmap.home.usescase

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month

@FlowPreview
class GetCountdownUseCase() {

    operator fun invoke() = flow {
        while (true) {
            emit(Duration.between(LocalDateTime.now(), LocalDateTime.of(2019, Month.AUGUST, 15, 18, 0, 0)))
            delay(1000)
        }
    }
}