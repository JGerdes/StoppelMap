package com.jonasgerdes.stoppelmap.model.map;

import io.realm.RealmObject;

/**
 * Created by Jonas on 27.07.2016.
 */
public class Tag extends RealmObject {

    private String name;

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
}
