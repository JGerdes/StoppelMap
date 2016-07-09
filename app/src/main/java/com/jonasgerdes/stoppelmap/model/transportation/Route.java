package com.jonasgerdes.stoppelmap.model.transportation;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonas on 08.07.2016.
 */
public class Route extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
    private RealmList<Station> stations;
    private RealmList<DepatureDay> returnTimes;

    public Route() {
    }

    public Route(int id, String name, RealmList<Station> stations, RealmList<DepatureDay> returnTimes) {
        this.id = id;
        this.name = name;
        this.stations = stations;
        this.returnTimes = returnTimes;
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

    public RealmList<Station> getStations() {
        return stations;
    }

    public void setStations(RealmList<Station> stations) {
        this.stations = stations;
    }

    public RealmList<DepatureDay> getReturnTimes() {
        return returnTimes;
    }

    public void setReturnTimes(RealmList<DepatureDay> returnTimes) {
        this.returnTimes = returnTimes;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stations=" + stations +
                ", returnTimes=" + returnTimes +
                '}';
    }
}
