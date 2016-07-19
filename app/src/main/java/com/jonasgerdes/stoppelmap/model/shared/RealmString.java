package com.jonasgerdes.stoppelmap.model.shared;

import io.realm.RealmObject;

/**
 * Created by Jonas on 19.07.2016.
 */
public class RealmString extends RealmObject {
    private String val;

    public RealmString() {
    }

    public RealmString(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
