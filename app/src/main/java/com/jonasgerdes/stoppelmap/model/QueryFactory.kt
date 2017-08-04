package com.jonasgerdes.stoppelmap.model

import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.model.entity.map.search.GroupSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.util.asRxObservable
import com.jonasgerdes.stoppelmap.util.asset.StringResourceHelper
import io.reactivex.Observable
import io.realm.RealmQuery
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.17
 */

class QueryFactory {

    @Inject lateinit var stringHelper: StringResourceHelper

    init {
        App.graph.inject(this)
    }

    fun createAttributeQuery(query: RealmQuery<MapEntity>, term: String, keywords: Int,
                             drawable: Int, vararg attributes: Pair<String, Any>)
            : Observable<List<MapSearchResult>>? {
        var realmQuery = query
        if (stringHelper.get(keywords).contains(term, true)) {
            attributes.forEach {
                when (it.second) {
                    is Boolean -> realmQuery = realmQuery.equalTo(it.first, it.second as Boolean)
                    is String -> realmQuery = realmQuery.equalTo(it.first, it.second as String)
                }
            }
            return realmQuery
                    .findAllAsync()
                    .asRxObservable()
                    .map { entities ->
                        listOf(GroupSearchResult(
                                stringHelper.get(keywords),
                                drawable,
                                entities))
                    }
        }
        return null
    }
}