package com.jonasgerdes.stoppelmap.usecase.map.viewmodel

import android.arch.lifecycle.ViewModel
import android.location.Location
import android.net.Uri
import android.util.Log
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.Settings
import com.jonasgerdes.stoppelmap.model.MapEntityRepository
import com.jonasgerdes.stoppelmap.model.entity.map.Exhibition
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.model.entity.map.SellerStall
import com.jonasgerdes.stoppelmap.model.entity.map.boundsFor
import com.jonasgerdes.stoppelmap.model.entity.map.search.GroupSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.ProductSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.SingleEntitySearchResult
import com.jonasgerdes.stoppelmap.util.map.latLng
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
class MapInteractor : ViewModel() {

    @Inject
    protected lateinit var repository: MapEntityRepository

    init {
        App.graph.inject(this)
    }

    private val stateSubject = BehaviorSubject.createDefault<MapViewState>(MapViewState.Exploring(
            GoogleMapState(
                    MapBounds(
                            Settings.cameraBounds,
                            Settings.minZoom,
                            Settings.maxZoom
                    ),
                    Settings.center,
                    Settings.defaultZoom
            ),
            repository.getVisibleEntities(Settings.defaultZoom)

    ))

    val state get() = stateSubject.hide()
            //todo: remove filter when sellerstands and exhibition have proper names
            .filter {
                !(it is MapViewState.EntityDetail &&
                        (it.entity.type == Exhibition.TYPE || it.entity.type == SellerStall.TYPE))
            }

    fun onIntentReceived(uri: Uri) {
        if(uri.pathSegments.size > 2) {
            val entity = repository.getEntityBySlug(uri.pathSegments[2])
            stateSubject.onNext(
                    if (entity != null) {
                        MapViewState.EntityDetail(
                                stateSubject.value.mapState.copy(
                                        zoom = entity.zoomLevel,
                                        center = entity.center.latLng
                                ),
                                entity
                        )
                    } else {
                        MapViewState.Exploring(
                                stateSubject.value.mapState.copy(),
                                repository.getVisibleEntities(stateSubject.value.mapState.zoom!!)
                        )
                    }
            )
        }
    }

    fun onMapMoved(position: CameraPosition) {
        Log.d("MapInteractor", "zoom: " + position.zoom)
        stateSubject.onNext(
                when (stateSubject.value) {
                    is MapViewState.EntityDetail -> MapViewState.EntityDetail(
                            stateSubject.value.mapState.copy(
                                    zoom = position.zoom,
                                    center = position.target,
                                    bounds = null
                            ),
                            (stateSubject.value as MapViewState.EntityDetail).entity
                    )
                    is MapViewState.EntityGroupDetail -> MapViewState.EntityGroupDetail(
                            stateSubject.value.mapState.copy(
                                    zoom = position.zoom,
                                    center = position.target,
                                    bounds = null
                            ),
                            (stateSubject.value as MapViewState.EntityGroupDetail).entities
                    )
                    else -> MapViewState.Exploring(
                            stateSubject.value.mapState.copy(
                                    zoom = position.zoom,
                                    center = position.target,
                                    bounds = null
                            ),
                            repository.getVisibleEntities(position.zoom))

                })
    }

    fun onUserMoved(location: Location) {
        stateSubject.onNext(
                if (Settings.cameraBounds.contains(location.latLng())) {
                    MapViewState.Exploring(
                            stateSubject.value.mapState.copy(
                                    center = location.latLng()
                            ),
                            repository.getVisibleEntities(stateSubject.value.mapState.zoom!!))
                } else {
                    MapViewState.Exploring(
                            stateSubject.value.mapState.copy(),
                            stateSubject.value.visibleEntities,
                            R.string.map_my_location_not_in_bounds)
                })
    }

    fun onMapClicked(position: LatLng) {
        val entity = repository.getEntityOn(position)
        stateSubject.onNext(
                if (entity != null) {
                    MapViewState.EntityDetail(
                            stateSubject.value.mapState.copy(
                                    zoom = entity.zoomLevel,
                                    center = entity.center.latLng
                            ),
                            entity
                    )
                } else {
                    MapViewState.Exploring(
                            stateSubject.value.mapState.copy(),
                            repository.getVisibleEntities(stateSubject.value.mapState.zoom!!)
                    )
                }
        )
    }

    fun onSearchChanged(term: String) {
        stateSubject.onNext(MapViewState.Searching(
                stateSubject.value.mapState.copy(),
                stateSubject.value.visibleEntities,
                term,
                repository.searchFor(term)
        ))
    }

    fun onSearchResultSelected(result: MapSearchResult) {
        when (result) {
            is SingleEntitySearchResult -> stateSubject.onNext(
                    MapViewState.EntityDetail(
                            stateSubject.value.mapState.copy(
                                    zoom = result.entity.zoomLevel,
                                    center = result.entity.center.latLng
                            ),
                            result.entity
                    )
            )
            is ProductSearchResult -> {
                val bounds = MapEntity.boundsFor(result.entities)
                stateSubject.onNext(MapViewState.EntityGroupDetail(
                        stateSubject.value.mapState.copy(
                                zoom = null,
                                center = null,
                                bounds = bounds
                        ),
                        result.entities
                ))
            }
            is GroupSearchResult -> {
                val bounds = MapEntity.boundsFor(result.entities)
                stateSubject.onNext(MapViewState.EntityGroupDetail(
                        stateSubject.value.mapState.copy(
                                zoom = null,
                                center = null,
                                bounds = bounds
                        ),
                        result.entities
                ))
            }
        }
    }

    fun onBottomSheetClosed(state: Int) {
        stateSubject.onNext(MapViewState.Exploring(
                stateSubject.value.mapState.copy(),
                stateSubject.value.visibleEntities
        ))
    }

    fun onShare(unit: Unit) {
        if (stateSubject.value is MapViewState.EntityDetail) {
            stateSubject.onNext(MapViewState.EntityDetail(
                    stateSubject.value.mapState.copy(),
                    (stateSubject.value as MapViewState.EntityDetail).entity,
                    true
            ))
        }
    }

    override fun onCleared() {
        if (!repository.isDisposed) {
            repository.dispose()
        }
        super.onCleared()
    }
}