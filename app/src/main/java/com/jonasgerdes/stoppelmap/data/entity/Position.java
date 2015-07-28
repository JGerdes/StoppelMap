package com.jonasgerdes.stoppelmap.data.entity;


import com.google.android.gms.maps.model.LatLng;

public class Position {
    public double lon;
    public double lat;

    public LatLng latLng(){
        return new LatLng(lat, lon);
    }
}
