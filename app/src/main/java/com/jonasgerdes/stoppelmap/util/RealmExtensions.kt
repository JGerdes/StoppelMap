package com.jonasgerdes.stoppelmap.util

import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 06-Jul-17
 */

fun <E : RealmObject> E.asRxObservable(): Observable<E> {
    return Observable.create<E> { emitter ->
        val listener = RealmChangeListener<E> { emitter.onNext(it) }
        emitter.setDisposable(Disposables.fromRunnable { removeChangeListener(listener) })
        addChangeListener(listener)
        emitter.onNext(this)
    }
}


fun <E : RealmModel> RealmResults<E>.asRxObservable(): Observable<RealmResults<E>> {
    return Observable.create<RealmResults<E>> { emitter ->
        val listener = RealmChangeListener<RealmResults<E>> { emitter.onNext(it) }
        emitter.setDisposable(Disposables.fromRunnable { removeChangeListener(listener) })
        addChangeListener(listener)
        emitter.onNext(this)
    }
}

fun <E : RealmModel> RealmResults<E>.asList(): List<E> {
    return this
}