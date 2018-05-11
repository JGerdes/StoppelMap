package com.jonasgerdes.stoppelmap.util

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun <T : Any> Observable<T>.delayIf(duration: Long,
                                           timeUnit: TimeUnit,
                                           predicate: (item: T) -> Boolean): Observable<T> {
    return concatMap {
        Observable.just(it).delay(if (predicate(it)) duration else 0L, timeUnit)
    }
}