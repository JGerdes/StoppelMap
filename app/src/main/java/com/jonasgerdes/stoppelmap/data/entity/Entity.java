package com.jonasgerdes.stoppelmap.data.entity;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Entity {
    public String title;
    public Position position;
    public float minZoom;
    public float maxZoom;
    public String[] labels;
    public String[] tags;

    public MarkerOptions markerOptions;
    private Marker currentMarker;

    public void addMarkerTo(GoogleMap map){
        if(currentMarker == null){
            currentMarker = map.addMarker(markerOptions);
        }
    }

    public void removeMarkerFrom(GoogleMap map){
        if(currentMarker != null){
            currentMarker.remove();
            currentMarker = null;
        }
    }
}
