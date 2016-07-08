package com.jonasgerdes.stoppelmap.model.transportation;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Jonas on 08.07.2016.
 */
public class DepatureDay extends RealmObject {

    private int id;
    private Date date;
    private RealmList<Depature> depatures;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RealmList<Depature> getDepatures() {
        return depatures;
    }

    public void setDepatures(RealmList<Depature> depatures) {
        this.depatures = depatures;
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
                ", date=" + date +
                ", depatures=" + depatures +
                ", comment='" + comment + '\'' +
                '}';
    }
}
