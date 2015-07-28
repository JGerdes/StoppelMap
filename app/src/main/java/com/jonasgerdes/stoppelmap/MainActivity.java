package com.jonasgerdes.stoppelmap;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final double LAT_MIN = 52.745;
    public static final double LAT_MAX = 52.750;
    public static final double LONG_MIN = 8.292;
    public static final double LOT_MAX = 8.299;
    public static final float ZOOM_MAX = 15;
    public static final float ZOOM_MIN = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(final GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NONE);
        map.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomMapTileProvider(getResources().getAssets())));
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.747995, 8.295607), 16));
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("camera", "lon:" + cameraPosition.target.longitude + "; lat:" + cameraPosition.target.latitude+"; z:"+cameraPosition.zoom);
                LatLng newPos = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
                float zoom = cameraPosition.zoom;
                if (cameraPosition.target.latitude > LAT_MAX){
                    newPos = new LatLng(LAT_MAX, newPos.longitude);
                }
                if (cameraPosition.target.longitude < LONG_MIN){
                    newPos = new LatLng(newPos.latitude, LONG_MIN);
                }
                if (cameraPosition.target.latitude < LAT_MIN){
                    newPos = new LatLng(LAT_MIN, newPos.longitude);
                }
                if (cameraPosition.target.longitude > LOT_MAX){
                    newPos = new LatLng(newPos.latitude, LOT_MAX);
                }
                zoom = Math.max(Math.min(zoom, ZOOM_MIN), ZOOM_MAX);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(newPos, zoom));
            }
        });
    }
}
