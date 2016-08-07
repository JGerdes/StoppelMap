package com.jonasgerdes.stoppelmap.model.transportation;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by Jonas on 07.08.2016.
 */
public class Taxi extends RealmObject {
    private String name;
    private String phone;
    private String phoneText;
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneText() {
        return phoneText;
    }

    public void setPhoneText(String phoneText) {
        this.phoneText = phoneText;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public static class Taxis{
        public List<Taxi> taxis;
    }
}
