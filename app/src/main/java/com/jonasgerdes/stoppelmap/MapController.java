package com.jonasgerdes.stoppelmap;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.jonasgerdes.stoppelmap.data.DataController;


public class MapController implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener {

    public static final double LAT_MIN = 52.745;
    public static final double LAT_MAX = 52.750;
    public static final double LONG_MIN = 8.292;
    public static final double LOT_MAX = 8.299;
    public static final float ZOOM_MAX = 15;
    public static final float ZOOM_MIN = 20;

    private GoogleMap map;
    private Context context;
    DataController data;

    public MapController(Context context){
        this.context = context;

        AssetManager assets = context.getResources().getAssets();
        data = new DataController(assets);
        data.readData();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data.createLabels(inflater);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        AssetManager assets = context.getResources().getAssets();

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NONE);
        map.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomMapTileProvider(assets)));
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.747995, 8.295607), 16));
        map.setOnCameraChangeListener(this);

        data.placeRelevantMarkers(map);

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d("camera", "lon:" + cameraPosition.target.longitude + "; lat:" + cameraPosition.target.latitude + "; z:" + cameraPosition.zoom);
        LatLng newPos = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
        float zoom = cameraPosition.zoom;
        boolean dirty = false;
        if (cameraPosition.target.latitude > LAT_MAX) {
            newPos = new LatLng(LAT_MAX, newPos.longitude);
            dirty = true;
        }
        if (cameraPosition.target.longitude < LONG_MIN) {
            newPos = new LatLng(newPos.latitude, LONG_MIN);
            dirty = true;
        }
        if (cameraPosition.target.latitude < LAT_MIN) {
            newPos = new LatLng(LAT_MIN, newPos.longitude);
            dirty = true;
        }
        if (cameraPosition.target.longitude > LOT_MAX) {
            newPos = new LatLng(newPos.latitude, LOT_MAX);
            dirty = true;
        }
        if(zoom > ZOOM_MIN || zoom < ZOOM_MAX){
            zoom = Math.max(Math.min(zoom, ZOOM_MIN), ZOOM_MAX);
            dirty = true;
        }
        if (dirty) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(newPos, zoom));
        }
        data.placeRelevantMarkers(map);
    }
}
