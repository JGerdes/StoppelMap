package com.jonasgerdes.stoppelmap.model.map;

import io.realm.RealmObject;

/**
 * Created by Jonas on 30.07.2016.
 */
public class Zoom extends RealmObject {

    public static final int NONE = -1;

    int min = NONE;
    int max = NONE;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
