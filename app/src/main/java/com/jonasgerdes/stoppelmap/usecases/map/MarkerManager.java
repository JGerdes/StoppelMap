package com.jonasgerdes.stoppelmap.usecases.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonas on 21.07.2016.
 */
public class MarkerManager {

    private Context mContext;
    private GoogleMap mMap;
    private Map<String, MapEntity> mMarkerEntityMap;
    private List<Marker> mMarkers;

    public MarkerManager(Context context, GoogleMap map) {
        mContext = context;
        mMap = map;
        mMarkers = new ArrayList<>();
        mMarkerEntityMap = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public void generateMarkers(final List<MapEntity> entities) {
        new LabelCreationTask(mContext)
                .onReady(new LabelCreationTask.OnReadyListener() {
                    @Override
                    public void onReady() {
                        placeRelevantMarkers(entities);
                    }
                })
                .execute(entities);
    }

    public void placeRelevantMarkers(List<MapEntity> entities) {
        for (Marker marker : mMarkers) {
            marker.remove();
        }
        mMarkers.clear();
        mMarkerEntityMap.clear();
        for (MapEntity mapEntity : entities) {
            if (mapEntity.getMarkerOptions() != null) {
                Marker temp = mMap.addMarker(mapEntity.getMarkerOptions());
                mMarkers.add(temp);
                mMarkerEntityMap.put(temp.getId(), mapEntity);
            }
        }
    }

    public MapEntity getEntityForMarker(Marker marker) {
        return mMarkerEntityMap.get(marker.getId());
    }
}
