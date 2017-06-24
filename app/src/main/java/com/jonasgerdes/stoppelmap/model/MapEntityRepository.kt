package com.jonasgerdes.stoppelmap.model

import io.reactivex.disposables.Disposable
import io.realm.Realm

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.06.2017
 */
class MapEntityRepository(val realm: Realm) : Disposable {
    override fun isDisposed(): Boolean {
        return realm.isClosed
    }

    override fun dispose() {
        realm.close()
    }
}