package com.jonasgerdes.stoppelmap.model.shared;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by Jonas on 11.08.2016.
 */
public class PhoneNumber extends RealmObject {

    private String name;
    private String phone;
    private String phoneText;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class PhoneNumbers {
        public List<PhoneNumber> mPhoneNumbers;
    }
}
