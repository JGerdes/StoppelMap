package com.jonasgerdes.stoppelmap.model.transportation;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonas on 08.07.2016.
 */
public class Departure extends RealmObject {

    @PrimaryKey
    private int id;
    private Date time;
    private String comment;

    public Departure() {
    }

    public Departure(int id, Date time, String comment) {
        this.id = id;
        this.time = time;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", time=" + time +
                ", comment='" + comment + '\'' +
                '}';
    }
}
