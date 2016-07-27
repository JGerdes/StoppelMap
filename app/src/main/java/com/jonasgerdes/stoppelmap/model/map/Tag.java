package com.jonasgerdes.stoppelmap.model.map;

import android.support.annotation.DrawableRes;

import io.realm.RealmObject;

/**
 * Created by Jonas on 27.07.2016.
 */
public class Tag extends RealmObject {

    public static final int ICON_NONE = -1;

    private String name;
    private
    @DrawableRes
    int icon = ICON_NONE;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Tag) {
            return ((Tag) o).getName().equals(name);
        }
        return false;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }
}
