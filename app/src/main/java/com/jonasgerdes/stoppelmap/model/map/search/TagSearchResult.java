package com.jonasgerdes.stoppelmap.model.map.search;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.map.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 27.07.2016.
 */
public class TagSearchResult extends SearchResult {

    private List<MapEntity> mMapEntities;
    private Tag mTag;

    public TagSearchResult(Tag tag) {
        mMapEntities = new ArrayList<>();
        mTag = tag;
    }

    @Override
    public CameraUpdate getCameraUpdate() {
        return getCameraUpdate(null);
    }

    @Override
    public CameraUpdate getCameraUpdate(LatLng userLocation) {
        LatLngBounds.Builder boundBuilder = new LatLngBounds.Builder();
        if (userLocation != null) {
            boundBuilder.include(userLocation);
        }
        for (MapEntity entity : mMapEntities) {
            boundBuilder.include(entity.getOrigin().toLatLng());
        }
        return CameraUpdateFactory.newLatLngBounds(boundBuilder.build(), 128);
    }

    @Override
    public String getIdentifier() {
        return mTag.getName();
    }

    @Override
    public int getLayout() {
        return R.layout.map_search_tag_result_item;
    }

    public List<MapEntity> getMapEntities() {
        return mMapEntities;
    }

    public void addEntity(MapEntity entity) {
        mMapEntities.add(entity);
    }

    public Tag getTag() {
        return mTag;
    }
}
