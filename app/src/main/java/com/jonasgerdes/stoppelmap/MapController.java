package com.jonasgerdes.stoppelmap;

import android.content.Context;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.jonasgerdes.stoppelmap.data.DataController;
import com.jonasgerdes.stoppelmap.data.LabelCreationTask;
import com.jonasgerdes.stoppelmap.data.SearchResult;


public class MapController implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener {

    public static final double LAT_MIN = 52.745;
    public static final double LAT_MAX = 52.750;
    public static final double LON_MIN = 8.292;
    public static final double LON_MAX = 8.299;
    public static final float ZOOM_MAX = 15;
    public static final float ZOOM_MIN = 20;

    private GoogleMap map;
    private Context context;
    private DataController data;
    private SearchResult currentSelection;
    private LocationManager locationManager;

    private LatLngBounds bounds;

    private Handler mapHandler;

    public MapController(Context context){
        this.context = context;

        AssetManager assets = context.getResources().getAssets();
        data = new DataController(assets);
        data.readData();

        //data.createLabels(inflater);

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        bounds = new LatLngBounds(new LatLng(LAT_MIN, LON_MIN), new LatLng(LAT_MAX, LON_MAX));
        mapHandler = new Handler();


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        AssetManager assets = context.getResources().getAssets();

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NONE);
        map.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomMapTileProvider(assets)));
        map.setMyLocationEnabled(true);

        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.747995, 8.295607), 16));
        map.setOnCameraChangeListener(this);


        final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //data.createLabels(inflater);
        //data.placeRelevantMarkers(map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                data.createLabels(inflater);
                mapHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        data.placeRelevantMarkers(map);
                    }
                });
            }
        }).start();


    }

    public boolean targetCurrentLocation(){
        LatLng pos = getPositionInBoundsOrNull();
        if(pos != null){
            map.animateCamera(CameraUpdateFactory.newLatLng(pos));
            return true;
        }
        return false;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        //Log.d("camera", "lon:" + cameraPosition.target.longitude + "; lat:" + cameraPosition.target.latitude + "; z:" + cameraPosition.zoom);
        LatLng newPos = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
        float zoom = cameraPosition.zoom;
        boolean dirty = false;
        if ((cameraPosition.target.latitude - LAT_MAX) > 0.0001) {
            Log.i("MC", "deltaLat:"+(cameraPosition.target.latitude - LAT_MAX));
            newPos = new LatLng(LAT_MAX, newPos.longitude);
            dirty = true;
        }
        if ((cameraPosition.target.longitude - LON_MIN) < -0.01) {
            Log.i("MC", "deltaLon:"+(cameraPosition.target.longitude - LON_MIN));
            newPos = new LatLng(newPos.latitude, LON_MIN);
            dirty = true;
        }
        if ((cameraPosition.target.latitude - LAT_MIN) < -0.0001) {
            Log.i("MC", "deltaLat:"+(cameraPosition.target.latitude - LAT_MIN));
            newPos = new LatLng(LAT_MIN, newPos.longitude);
            dirty = true;
        }
        if ((cameraPosition.target.longitude - LON_MAX) > 0.01) {
            Log.i("MC", "deltaLon:"+(cameraPosition.target.longitude - LON_MAX));
            newPos = new LatLng(newPos.latitude, LON_MAX);
            dirty = true;
        }
        if(zoom - ZOOM_MIN > 0.2 || zoom - ZOOM_MAX < -0.2){
            Log.i("MC", "deltaZoom:"+(zoom - ZOOM_MIN) + ", "+(zoom - ZOOM_MAX));
            zoom = Math.max(Math.min(zoom, ZOOM_MIN), ZOOM_MAX);
            dirty = true;
        }
        if (dirty) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(newPos, zoom));
        }
        if(currentSelection == null){
            data.placeRelevantMarkers(map);
        }

    }

    private LatLng getPositionInBoundsOrNull(){
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location == null){
            return null;
        }
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        if(bounds.contains(pos)){
            Log.d("MC", "pos in bounds:"+pos.toString());
            return pos;
        }else{
            Log.d("MC", "pos not in bounds");
            return null;
        }

    }



    public DataController getData(){
        return data;
    }

    public void present(SearchResult result){
        currentSelection = result;
        if(currentSelection != null){
            data.removeAllMarkers(map);
            currentSelection.placeRelevantMarker(map);
            map.animateCamera(currentSelection.getCameraUpdate(getPositionInBoundsOrNull()));
        }else{
            data.placeRelevantMarkers(map);
            //zoom in/out a bit
   /*         CameraPosition pos = map.getCameraPosition();
            LatLng latlng = new LatLng(pos.target.latitude, pos.target.longitude);
            float zoom =  pos.zoom;
            if(zoom >= 19){
                zoom -= 0.5f;
            }else{
                zoom += 0.5;
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
            */
        }

    }
}
