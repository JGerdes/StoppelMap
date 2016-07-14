package com.jonasgerdes.stoppelmap.model.transportation;

import android.support.annotation.IntDef;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

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

    @PrimaryKey
    private int id;
    private @Day int day;
    private RealmList<Departure> mDepartures;
    private String comment;

    public DepartureDay() {
    }

    public DepartureDay(int id, @Day int day, RealmList<Departure> departures, String comment) {
        this.id = id;
        this.day = day;
        this.mDepartures = departures;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @Day int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public RealmList<Departure> getDepartures() {
        return mDepartures;
    }

    public void setDepartures(RealmList<Departure> departures) {
        this.mDepartures = departures;
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
                "id=" + id +
                ", day=" + day +
                ", depatures=" + mDepartures +
                ", comment='" + comment + '\'' +
                '}';
    }
}
