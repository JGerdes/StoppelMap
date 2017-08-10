package com.jonasgerdes.stoppelmap.model

import com.jonasgerdes.stoppelmap.model.events.Event
import com.jonasgerdes.stoppelmap.util.asList
import com.jonasgerdes.stoppelmap.util.asRxObservable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.realm.Realm
import io.realm.Sort

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 10.08.2017
 */
class EventRepository : Disposable {

    private val realm: Realm = Realm.getDefaultInstance()

    fun getEventsFor(day: Int): Observable<List<Event>> {
        return realm.where(Event::class.java)
                .equalTo("day", day)
                .findAll().sort("start", Sort.ASCENDING)
                .asRxObservable()
                .map { it.asList() }
    }

    override fun isDisposed(): Boolean {
        return realm.isClosed
    }

    override fun dispose() {
        realm.close()
    }

}
