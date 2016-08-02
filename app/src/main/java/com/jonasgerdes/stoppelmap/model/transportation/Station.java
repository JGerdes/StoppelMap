package com.jonasgerdes.stoppelmap.model.transportation;

import com.jonasgerdes.stoppelmap.model.shared.GeoLocation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonas on 08.07.2016.
 */
public class Station extends RealmObject {

    @PrimaryKey
    private String uuid;
    private String name;
    private GeoLocation geoLocation;
    private RealmList<Price> prices;
    private RealmList<DepartureDay> days;

    public Station() {
    }

    public Station(String uuid, String name, GeoLocation geoLocation, RealmList<DepartureDay> days) {
        this.uuid = uuid;
        this.name = name;
        this.geoLocation = geoLocation;
        this.days = days;
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

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public RealmList<DepartureDay> getDays() {
        return days;
    }

    public void setDays(RealmList<DepartureDay> days) {
        this.days = days;
    }

    public RealmList<Price> getPrices() {
        return prices;
    }

    public void setPrices(RealmList<Price> prices) {
        this.prices = prices;
    }

    @Override
    public String toString() {
        return "Station{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", geoLocation=" + geoLocation +
                ", days=" + days +
                '}';
    }

    public Departure getNextDepature(Calendar date) {
        Calendar departureTime = Calendar.getInstance();
        for (DepartureDay day : days) {
            for (Departure departure : day.getDepartures()) {
                departureTime.setTime(departure.getTime());
                if (departureTime.after(date)) {
                    return departure;
                }
            }
        }
        return null;
    }


    public List<Departure> getNextDepatures(Calendar date, int count) {
        List<Departure> nextDepatures = new ArrayList<>(count);
        Calendar departureTime = Calendar.getInstance();
        for (DepartureDay day : days) {
            boolean addNext = false;
            for (Departure departure : day.getDepartures()) {
                departureTime.setTime(departure.getTime());
                if (departureTime.after(date)) {
                    addNext = true;
                }
                if (addNext) {
                    nextDepatures.add(departure);
                }
                if (nextDepatures.size() == count) {
                    break;
                }
            }
        }
        return nextDepatures;
    }
}
