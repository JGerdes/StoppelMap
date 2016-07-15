package com.jonasgerdes.stoppelmap.model.transportation;

import io.realm.RealmObject;

/**
 * Created by Jonas on 15.07.2016.
 */
public class Price extends RealmObject {

    private String type;
    private int price;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
