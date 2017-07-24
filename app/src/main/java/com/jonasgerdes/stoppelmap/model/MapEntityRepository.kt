package com.jonasgerdes.stoppelmap.model

import com.google.android.gms.maps.model.LatLng
import com.jonasgerdes.stoppelmap.Settings
import com.jonasgerdes.stoppelmap.model.entity.Product
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.ProductSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.SingleEntitySearchResult
import com.jonasgerdes.stoppelmap.util.asList
import com.jonasgerdes.stoppelmap.util.asRxObservable
import com.jonasgerdes.stoppelmap.util.asset.Assets
import com.jonasgerdes.stoppelmap.util.map.isIn
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.zip
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.06.2017
 */
class MapEntityRepository : Disposable {

    private val realm: Realm = Realm.getDefaultInstance()

    fun searchFor(term: String): Observable<List<MapSearchResult>> {
        return if (term.isEmpty()) {
            Observable.just(ArrayList())
        } else {
            arrayListOf(
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
                            .like("name", "*$term*", Case.INSENSITIVE)
                            .findAllAsync()
                            .asRxObservable()
                            .map { products -> createProductResult(products, term) }
            ).zip {
                val result = ArrayList<MapSearchResult>()
                it.forEach {
                    result.addAll(it)
                }
                result
            }.map { it.distinctBy { it.title } }
        }
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
                            .like("foodStall.dishes.name", product.name).findAll()
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
        if (zoom < Settings.detailZoom) {
            return emptyList()
        } else {
            return realm.where(MapEntity::class.java)
                    .findAll()
                    .asList()
        }
    }

    fun getEntityOn(position: LatLng): MapEntity? {
        return realm.where(MapEntity::class.java)
                .findAll()
                .firstOrNull { position.isIn(it.bounds) }
    }
}