package com.jonasgerdes.stoppelmap.model.transportation;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Jonas on 08.07.2016.
 */
public class Departure extends RealmObject {

    private Date time;
    private String comment;

    public Departure() {
    }

    public Departure(Date time, String comment) {
        this.time = time;
        this.comment = comment;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Depature{" +
                "time=" + time +
                ", comment='" + comment + '\'' +
                '}';
    }
}
