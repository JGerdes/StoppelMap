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
    private GeoLocation geoLoation;
    private RealmList<DepatureDay> depatures;

    public Station() {
    }

    public Station(int id, String name, GeoLocation geoLoation, RealmList<DepatureDay> depatures) {
        this.id = id;
        this.name = name;
        this.geoLoation = geoLoation;
        this.depatures = depatures;
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

    public GeoLocation getGeoLoation() {
        return geoLoation;
    }

    public void setGeoLoation(GeoLocation geoLoation) {
        this.geoLoation = geoLoation;
    }

    public RealmList<DepatureDay> getDepatures() {
        return depatures;
    }

    public void setDepatures(RealmList<DepatureDay> depatures) {
        this.depatures = depatures;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", geoLoation=" + geoLoation +
                ", depatures=" + depatures +
                '}';
    }
}
