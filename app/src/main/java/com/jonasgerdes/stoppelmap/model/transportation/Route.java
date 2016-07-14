package com.jonasgerdes.stoppelmap.model.transportation;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonas on 08.07.2016.
 */
public class Route extends RealmObject {

    @PrimaryKey
    private String uuid;
    private String name;
    private RealmList<Station> stations;
    private RealmList<DepartureDay> returnTimes;

    public Route() {
    }

    public Route(String uuid, String name, RealmList<Station> stations, RealmList<DepartureDay> returnTimes) {
        this.uuid = uuid;
        this.name = name;
        this.stations = stations;
        this.returnTimes = returnTimes;
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

    public RealmList<DepartureDay> getReturnTimes() {
        return returnTimes;
    }

    public void setReturnTimes(RealmList<DepartureDay> returnTimes) {
        this.returnTimes = returnTimes;
    }

    @Override
    public String toString() {
        return "Route{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", stations=" + stations +
                ", returnTimes=" + returnTimes +
                '}';
    }
}
