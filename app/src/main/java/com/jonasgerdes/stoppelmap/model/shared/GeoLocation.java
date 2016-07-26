package com.jonasgerdes.stoppelmap.model.shared;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmObject;

/**
 * Created by Jonas on 08.07.2016.
 */

public class GeoLocation extends RealmObject {

    public GeoLocation() {
    }

    public GeoLocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    private double lat;
    private double lng;

    public LatLng toLatLng() {
        return new LatLng(lat, lng);
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
