package com.jonasgerdes.stoppelmap.model.transportation;


import android.location.Location;
import android.support.v4.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.jonasgerdes.stoppelmap.model.shared.GeoLocation;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonas on 08.07.2016.
 */
public class Route extends RealmObject {

    @PrimaryKey
    private String uuid;
    private String name;
    private RealmList<Station> stations;
    private Station returnStation;

    @Ignore
    private Pair<Station, Float> mNearestStation;

    public Route() {
    }

    public Route(String uuid, String name, RealmList<Station> stations, Station returnStation) {
        this.uuid = uuid;
        this.name = name;
        this.stations = stations;
        this.returnStation = returnStation;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Station> getStations() {
        return stations;
    }

    public void setStations(RealmList<Station> stations) {
        this.stations = stations;
    }

    public Station getReturnStation() {
        return returnStation;
    }

    public void setReturnStation(Station returnStation) {
        this.returnStation = returnStation;
    }

    @Override
    public String toString() {
        return "Route{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", stations=" + stations +
                ", returnStation=" + returnStation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Route && ((Route) o).getUuid().equals(uuid);
    }

    public Station getNearestStation(LatLng location) {
        if (mNearestStation == null) {
            updateNearestStation(location);
        }
        return mNearestStation.first;
    }

    public float getNearestStationDistance(LatLng location) {
        if (mNearestStation == null) {
            updateNearestStation(location);
        }
        return mNearestStation.second;
    }

    public void updateNearestStation(LatLng location) {
        Station nearest = null;
        float nearestDistance = 0;
        float[] distance = new float[1];
        for (Station station : getStations()) {
            GeoLocation stationLoc = station.getGeoLocation();
            Location.distanceBetween(
                    stationLoc.getLat(),
                    stationLoc.getLng(),
                    location.latitude,
                    location.longitude,
                    distance
            );
            if (nearest == null || distance[0] < nearestDistance) {
                nearest = station;
                nearestDistance = distance[0];
            }
        }
        mNearestStation = new Pair<>(nearest, nearestDistance);
    }
}
