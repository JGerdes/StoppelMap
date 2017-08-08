package com.jonasgerdes.stoppelmap.model

import com.google.android.gms.maps.model.LatLng
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.Product
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.model.entity.map.Restroom
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.ProductSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.SingleEntitySearchResult
import com.jonasgerdes.stoppelmap.util.addIfNotNull
import com.jonasgerdes.stoppelmap.util.asList
import com.jonasgerdes.stoppelmap.util.asRxObservable
import com.jonasgerdes.stoppelmap.util.asset.Assets
import com.jonasgerdes.stoppelmap.util.asset.StringResourceHelper
import com.jonasgerdes.stoppelmap.util.map.isIn
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.zip
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.06.2017
 */
class MapEntityRepository : Disposable {

    private val realm: Realm = Realm.getDefaultInstance()
    @Inject lateinit var stringHelper: StringResourceHelper
    @Inject lateinit var queryFactory: QueryFactory

    init {
        App.graph.inject(this)
    }

    fun searchFor(term: String): Observable<List<MapSearchResult>> {
        return if (term.isEmpty()) {
            Observable.just(ArrayList())
        } else {
            val results = arrayListOf(
                    realm.where(MapEntity::class.java)
                            .like("name", "*$term*", Case.INSENSITIVE)
                            .findAllAsync()
                            .asRxObservable()
                            .map { entities -> createEntityResult(entities, term) },
                    realm.where(MapEntity::class.java)
                            .like("alias.value", "*$term*", Case.INSENSITIVE)
                            .findAllAsync()
                            .asRxObservable()
                            .map { entities -> createEntityResult(entities, term) },
                    realm.where(Product::class.java)
                            .findAllAsync()
                            .asRxObservable()
                            .map {
                                it.filter {
                                    stringHelper.getNameFor(it).contains(term, true)
                                }.distinctBy { it.name }
                            }
                            .map { products -> createProductResult(products, term) }
            )
            results.addAll(createSpecialQueries(term))
            results.zip {
                val result = ArrayList<MapSearchResult>()
                it.forEach {
                    result.addAll(it)
                }
                result
            }.map { it.distinctBy { it.title } }
        }
    }

    private fun createSpecialQueries(term: String): List<Observable<List<MapSearchResult>>> {
        val queries: ArrayList<Observable<List<MapSearchResult>>> = ArrayList<Observable<List<MapSearchResult>>>()
        queries.addIfNotNull(queryFactory.createAttributeQuery(
                realm.where(MapEntity::class.java),
                term,
                R.array.restroom_synonyms,
                R.drawable.ic_entity_restroom_black_24dp,
                Pair("type", Restroom.TYPE)
        ))
        queries.addIfNotNull(queryFactory.createAttributeQuery(
                realm.where(MapEntity::class.java),
                term,
                R.string.restroom_women_only,
                R.drawable.ic_entity_restroom_black_24dp,
                Pair("type", Restroom.TYPE),
                Pair("restroom.forWomen", true)
        ))
        queries.addIfNotNull(queryFactory.createAttributeQuery(
                realm.where(MapEntity::class.java),
                term,
                R.string.restroom_men_only,
                R.drawable.ic_entity_restroom_black_24dp,
                Pair("type", Restroom.TYPE),
                Pair("restroom.forMen", true)
        ))
        queries.addIfNotNull(queryFactory.createAttributeQuery(
                realm.where(MapEntity::class.java),
                term,
                R.array.restroom_for_disabled_synonyms,
                R.drawable.ic_entity_restroom_black_24dp,
                Pair("type", Restroom.TYPE),
                Pair("restroom.forDisabled", true)
        ))
        return queries
    }

    private fun createProductResult(products: List<Product>?, term: String)
            : List<MapSearchResult> {
        if (products == null) {
            return emptyList()
        }
        return products.map { product ->
            ProductSearchResult(product,
                    Assets.getIconsFor(product),
                    realm.where(MapEntity::class.java)
                            .like("foodStall.dishes.name", product.name)
                            .or().like("bar.drinks.name", product.name)
                            .or().like("candyStall.products.name", product.name)
                            .or().like("gameStall.games.name", product.name)
                            .findAll()
            )
        }
    }

    private fun createEntityResult(entities: RealmResults<MapEntity>?, term: String)
            : List<MapSearchResult> {
        if (entities == null) {
            return emptyList()
        }
        return entities.map { mapEntity ->
            SingleEntitySearchResult(
                    mapEntity.name!!,
                    mapEntity,
                    mapEntity.alias.where()
                            .like("value", "*$term*", Case.INSENSITIVE).findFirst()?.value
            )
        }
    }

    override fun isDisposed(): Boolean {
        return realm.isClosed
    }

    override fun dispose() {
        realm.close()
    }

    fun getVisibleEntities(zoom: Float): List<MapEntity> {
        return realm.where(MapEntity::class.java)
                .lessThanOrEqualTo("zoomLevel", zoom)
                .findAll()
                .asList()
    }

    fun getEntityOn(position: LatLng): MapEntity? {
        return realm.where(MapEntity::class.java)
                .findAll()
                .firstOrNull { position.isIn(it.bounds) }
    }

    fun getEntityBySlug(slug: String): MapEntity? {
        return realm.where(MapEntity::class.java)
                .equalTo("slug", slug)
                .findAll()
                .firstOrNull()
    }
}
