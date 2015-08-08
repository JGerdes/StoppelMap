package com.jonasgerdes.stoppelmap.data.entity;


import com.google.android.gms.maps.model.LatLng;

public class Position {
    public double lon;
    public double lat;
    private LatLng pos;

    public LatLng latLng(){
        if(pos == null){
            pos = new LatLng(lat, lon);
        }
        return pos;
    }
}
