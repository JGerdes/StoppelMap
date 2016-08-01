package com.jonasgerdes.stoppelmap.model.map.search;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.map.Tag;
import com.jonasgerdes.stoppelmap.util.MapUtil;

import java.util.ArrayList;
import java.util.Collections;
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
        if (mMapEntities.size() > 1) {
            return getCameraUpdate(null);
        } else if (mMapEntities.size() == 1) {
            return CameraUpdateFactory
                    .newLatLngZoom(mMapEntities.get(0).getOrigin().toLatLng(), DEFAULT_SEARCH_ZOOM);
        } else {
            return null;
        }
    }

    @Override
    public CameraUpdate getCameraUpdate(LatLng userLocation) {
        LatLngBounds.Builder boundBuilder = new LatLngBounds.Builder();
        List<MapEntity> includedEntities = null;

        if (userLocation != null) {
            boundBuilder.include(userLocation);
            includedEntities = getNearestEntities(mMapEntities, userLocation, 2);
        } else {
            includedEntities = mMapEntities;
        }
        for (MapEntity entity : includedEntities) {
            boundBuilder.include(entity.getOrigin().toLatLng());
        }
        return CameraUpdateFactory.newLatLngBounds(boundBuilder.build(), 160);
    }

    private List<MapEntity> getNearestEntities(List<MapEntity> mapEntities, LatLng userLocation, int count) {
        if (mapEntities.size() < count) {
            return mapEntities;
        }
        List<MapEntity> result = new ArrayList<>(count);
        Collections.sort(mapEntities, MapUtil.getMapEntityDistanceComparator(userLocation));
        for (int i = 0; i < mapEntities.size() && i < count; i++) {
            result.add(mapEntities.get(i));
        }
        return result;
    }

    @Override
    public boolean containsEntity(MapEntity entity) {
        for (MapEntity mapEntity : mMapEntities) {
            if (mapEntity.getUuid().equals(entity.getUuid())) {
                return true;
            }
        }
        return false;
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
