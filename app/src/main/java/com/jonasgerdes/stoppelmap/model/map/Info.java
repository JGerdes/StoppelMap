package com.jonasgerdes.stoppelmap.model.map;

import io.realm.RealmObject;

/**
 * Created by Jonas on 20.07.2016.
 */
public class Info extends RealmObject {

    private String title;
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
