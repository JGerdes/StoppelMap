package com.jonasgerdes.stoppelmap.model

import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.SingleEntitySearchResult
import com.jonasgerdes.stoppelmap.util.asRxObservable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.06.2017
 */
class MapEntityRepository : Disposable {

    val realm: Realm = Realm.getDefaultInstance()

    fun searchFor(term: String): Observable<List<MapSearchResult>> {
        return realm.where(MapEntity::class.java)
                .like("name", "*$term*", Case.INSENSITIVE)
                .findAllAsync()
                .asRxObservable()
                .map { entities -> createEntityResult(entities, term) }
    }

    private fun createEntityResult(entities: RealmResults<MapEntity>?, term: String)
            : List<SingleEntitySearchResult> {
        if (entities == null) {
            return emptyList()
        }
        val result = ArrayList<SingleEntitySearchResult>()
        entities.forEach { mapEntity ->
            result.add(SingleEntitySearchResult(
                    mapEntity.name!!,
                    mapEntity
            ))
        }
        return result
    }

    override fun isDisposed(): Boolean {
        return realm.isClosed
    }

    override fun dispose() {
        realm.close()
    }
}