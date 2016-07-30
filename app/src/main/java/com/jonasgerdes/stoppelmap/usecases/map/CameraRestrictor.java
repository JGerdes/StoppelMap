package com.jonasgerdes.stoppelmap.usecases.map;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jonas on 30.07.2016.
 */
public class CameraRestrictor implements GoogleMap.OnCameraChangeListener {
    private static final double LAT_MIN = 52.743;    //SOUTH
    private static final double LAT_MAX = 52.750;    //NORTH
    private static final double LON_MIN = 8.290;     //WEST
    private static final double LON_MAX = 8.299;     //EAST
    private static final float ZOOM_MAX = 15;
    private static final float ZOOM_MIN = 20;

    private GoogleMap mMap;

    public CameraRestrictor(GoogleMap map) {
        mMap = map;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d("CameraUpdate", "current zoom:" + cameraPosition.zoom);
        LatLng newPos = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
        float zoom = cameraPosition.zoom;
        boolean dirty = false;
        if ((cameraPosition.target.latitude - LAT_MAX) > 0.0001) {
            newPos = new LatLng(LAT_MAX, newPos.longitude);
            dirty = true;
        }
        if ((cameraPosition.target.longitude - LON_MIN) < -0.001) {
            newPos = new LatLng(newPos.latitude, LON_MIN);
            dirty = true;
        }
        if ((cameraPosition.target.latitude - LAT_MIN) < -0.0001) {
            newPos = new LatLng(LAT_MIN, newPos.longitude);
            dirty = true;
        }
        if ((cameraPosition.target.longitude - LON_MAX) > 0.001) {
            newPos = new LatLng(newPos.latitude, LON_MAX);
            dirty = true;
        }
        if (zoom - ZOOM_MIN > 0.2 || zoom - ZOOM_MAX < -0.2) {
            zoom = Math.max(Math.min(zoom, ZOOM_MIN), ZOOM_MAX);
            dirty = true;
        }
        if (dirty) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newPos, zoom));
        }
    }
}
