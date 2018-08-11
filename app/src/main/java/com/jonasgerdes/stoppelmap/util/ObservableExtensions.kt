package com.jonasgerdes.stoppelmap.util

import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

fun <T : Any> Observable<T>.delayIf(duration: Long,
                                    timeUnit: TimeUnit,
                                    predicate: (item: T) -> Boolean): Observable<T> {
    return concatMap {
        Observable.just(it).delay(if (predicate(it)) duration else 0L, timeUnit)
    }
}

inline fun <T : Any> Observable<T>.log(tag: String, crossinline content: (item: T) -> String): Observable<T> {
    return doOnNext {
        Log.d(tag, content(it))
    }
}

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

infix fun Disposable.to(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}