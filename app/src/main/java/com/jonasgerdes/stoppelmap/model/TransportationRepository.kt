package com.jonasgerdes.stoppelmap.model

import com.jonasgerdes.stoppelmap.model.entity.Route
import com.jonasgerdes.stoppelmap.util.asList
import com.jonasgerdes.stoppelmap.util.asRxObservable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.realm.Realm
import io.realm.Sort

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.2017
 */
class TransportationRepository : Disposable {

    private val realm: Realm = Realm.getDefaultInstance()

    fun getRoutes(): Observable<List<Route>> {
        return realm.where(Route::class.java)
                .findAll().sort("name", Sort.ASCENDING)
                .asRxObservable()
                .map { it.asList() }
    }

    fun getRouteBy(slug: String): Route? {
        return realm.where(Route::class.java)
                .equalTo("uuid", slug)
                .findAll()
                .firstOrNull()
    }

    override fun isDisposed(): Boolean {
        return realm.isClosed
    }

    override fun dispose() {
        realm.close()
    }

}
