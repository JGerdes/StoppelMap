package com.jonasgerdes.stoppelmap.model.transportation;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Jonas on 08.07.2016.
 */
public class Depature extends RealmObject {

    private int id;
    private Date time;
    private String comment;

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
