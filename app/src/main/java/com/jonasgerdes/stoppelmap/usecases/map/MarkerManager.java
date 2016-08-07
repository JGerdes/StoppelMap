package com.jonasgerdes.stoppelmap.usecases.map;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.map.Zoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonas on 21.07.2016.
 */
public class MarkerManager implements GoogleMap.OnCameraChangeListener {
    private static final String TAG = "MarkerManager";

    private Context mContext;
    private GoogleMap mMap;
    private Map<String, MapEntity> mMarkerEntityMap;
    private List<Marker> mMarkers;
    private List<MapEntity> mVisibleEntities;
    private CameraPosition mCurrentCameraPosition;
    private boolean mIgnoreZoom;

    public MarkerManager(Context context, GoogleMap map) {
        mContext = context;
        mMap = map;
        mMarkers = new ArrayList<>();
        mMarkerEntityMap = new HashMap<>();
        mVisibleEntities = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public void generateMarkers(final List<MapEntity> entities) {
        generateMarkers(entities, null);
    }

    public void generateMarkers(final List<MapEntity> entities,
                                final LabelCreationTask.OnReadyListener listener) {
        new LabelCreationTask(mContext)
                .onReady(new LabelCreationTask.OnReadyListener() {
                    @Override
                    public void onReady() {
                        mVisibleEntities = entities;
                        placeRelevantMarkers();
                        if (listener != null) {
                            listener.onReady();
                        }
                    }
                })
                .execute(entities);
    }


    public void placeRelevantMarkers() {
        if(mMap == null) {
            Log.e(TAG, " GoogleMap is null, ignoring marker placement");
            return;
        }
        for (Marker marker : mMarkers) {
            marker.remove();
        }
        mMarkers.clear();
        mMarkerEntityMap.clear();
        for (MapEntity mapEntity : mVisibleEntities) {
            if (mapEntity.getMarkerOptions() != null) {
                if (isEntityVisible(mapEntity)) {
                    Marker temp = mMap.addMarker(mapEntity.getMarkerOptions());
                    mMarkers.add(temp);
                    mMarkerEntityMap.put(temp.getId(), mapEntity);
                }
            }
        }
    }

    private boolean isEntityVisible(MapEntity mapEntity) {
        if (mCurrentCameraPosition == null) {
            return false;
        } else if (mIgnoreZoom) {
            return true;
        } else {
            Zoom zoom = mapEntity.getZoom();
            return zoom.getMin() <= mCurrentCameraPosition.zoom
                    && (zoom.getMax() == Zoom.NONE || zoom.getMax() <= mCurrentCameraPosition.zoom);
        }
    }

    public MapEntity getEntityForMarker(Marker marker) {
        return mMarkerEntityMap.get(marker.getId());
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        boolean doUpdate = mCurrentCameraPosition == null ||
                cameraPosition.zoom != mCurrentCameraPosition.zoom;
        mCurrentCameraPosition = cameraPosition;
        if (doUpdate) {
            placeRelevantMarkers();
        }
    }

    public void setVisibleEntities(List<MapEntity> visibleEntities) {
        mVisibleEntities = visibleEntities;
    }

    public void setVisibleEntities(MapEntity... visibleEntities) {
        mVisibleEntities = new ArrayList<>();
        for (MapEntity visibleEntity : visibleEntities) {
            mVisibleEntities.add(visibleEntity);
        }
    }

    public void setIgnoreZoom(boolean ignoreZoom) {
        mIgnoreZoom = ignoreZoom;
    }

    public void destroy() {
        mMarkers.clear();
        mVisibleEntities.clear();
        mMarkerEntityMap.clear();
        mMap = null;
    }
}
