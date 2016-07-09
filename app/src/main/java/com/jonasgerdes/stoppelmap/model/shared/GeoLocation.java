package com.jonasgerdes.stoppelmap.model.shared;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmObject;

/**
 * Created by Jonas on 08.07.2016.
 */

public class GeoLocation extends RealmObject {

    public GeoLocation() {
    }

    public GeoLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    private double lat;
    private double lon;

    public LatLng toLatLng() {
        return new LatLng(lat, lon);
    }
}
