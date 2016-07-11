package com.jonasgerdes.stoppelmap.model.transportation;

import com.jonasgerdes.stoppelmap.model.shared.GeoLocation;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonas on 08.07.2016.
 */
public class Station extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
    private GeoLocation geoLocation;
    private RealmList<DepatureDay> days;

    public Station() {
    }

    public Station(int id, String name, GeoLocation geoLocation, RealmList<DepatureDay> days) {
        this.id = id;
        this.name = name;
        this.geoLocation = geoLocation;
        this.days = days;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public RealmList<DepatureDay> getDays() {
        return days;
    }

    public void setDays(RealmList<DepatureDay> days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", geoLocation=" + geoLocation +
                ", days=" + days +
                '}';
    }
}
