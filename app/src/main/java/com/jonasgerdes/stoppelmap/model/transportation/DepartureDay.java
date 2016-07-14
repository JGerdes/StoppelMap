package com.jonasgerdes.stoppelmap.model.transportation;

import android.support.annotation.IntDef;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Jonas on 08.07.2016.
 */
public class DepartureDay extends RealmObject {


    public static final int DAY_THURSDAY = 0;
    public static final int DAY_FRIDAY = 1;
    public static final int DAY_SATURDAY = 2;
    public static final int DAY_SUNDAY = 3;
    public static final int DAY_MONDAY = 4;
    public static final int DAY_TUESDAY = 5;

    @IntDef({
            DAY_THURSDAY,
            DAY_FRIDAY,
            DAY_SATURDAY,
            DAY_SUNDAY,
            DAY_MONDAY,
            DAY_TUESDAY
    })
    public @interface Day {
    }

    private
    @Day
    int day;
    private RealmList<Departure> departures;
    private String comment;

    public DepartureDay() {
    }

    public DepartureDay(@Day int day, RealmList<Departure> departures, String comment) {
        this.day = day;
        this.departures = departures;
        this.comment = comment;
    }

    public
    @Day
    int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public RealmList<Departure> getDepartures() {
        return departures;
    }

    public void setDepartures(RealmList<Departure> departures) {
        this.departures = departures;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "DepatureDay{" +
                "day=" + day +
                ", depatures=" + departures +
                ", comment='" + comment + '\'' +
                '}';
    }
}
