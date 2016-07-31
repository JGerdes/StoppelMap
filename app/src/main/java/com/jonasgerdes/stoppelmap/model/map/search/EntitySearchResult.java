package com.jonasgerdes.stoppelmap.model.map.search;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;

/**
 * Created by Jonas on 27.07.2016.
 */
public class EntitySearchResult extends SearchResult {

    private MapEntity mMapEntity;

    public EntitySearchResult(MapEntity mapEntity) {
        mMapEntity = mapEntity;
    }

    @Override
    public CameraUpdate getCameraUpdate() {
        return CameraUpdateFactory.newLatLngZoom(mMapEntity.getOrigin().toLatLng(), DEFAULT_SEARCH_ZOOM);
    }

    @Override
    public CameraUpdate getCameraUpdate(LatLng userLocation) {
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(userLocation)
                .include(mMapEntity.getOrigin().toLatLng())
                .build();
        return CameraUpdateFactory.newLatLngBounds(bounds, 128);
    }

    @Override
    public boolean containsEntity(MapEntity entity) {
        return mMapEntity.getUuid().equals(entity.getUuid());
    }

    @Override
    public String getIdentifier() {
        return mMapEntity.getUuid();
    }

    @Override
    public int getLayout() {
        return R.layout.map_search_entity_result_item;
    }

    public MapEntity getMapEntity() {
        return mMapEntity;
    }
}
