package com.jonasgerdes.stoppelmap.model

import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import io.reactivex.disposables.Disposable
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.06.2017
 */
class MapEntityRepository : Disposable {

    val realm: Realm

    init {
        realm = Realm.getDefaultInstance()
    }

    fun searchFor(term: String): RealmResults<MapEntity> {
        return realm.where(MapEntity::class.java)
                .like("name", "*$term*", Case.INSENSITIVE)
                .findAllAsync()
    }

    override fun isDisposed(): Boolean {
        return realm.isClosed
    }

    override fun dispose() {
        realm.close()
    }
}