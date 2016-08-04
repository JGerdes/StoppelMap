package com.jonasgerdes.stoppelmap.model.schedule;

import com.jonasgerdes.stoppelmap.model.map.Tag;
import com.jonasgerdes.stoppelmap.model.shared.RealmString;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Jonas on 04.08.2016.
 */
public class Event extends RealmObject {

    private String uuid;
    private String name;
    private int type;

    private int day;
    private Date start;
    private Date end;

    private String locationUuid;

    private String description;
    private String facebookUrl;
    private RealmList<RealmString> artists;
    private RealmList<Tag> tags;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getLocationUuid() {
        return locationUuid;
    }

    public void setLocationUuid(String locationUuid) {
        this.locationUuid = locationUuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public RealmList<RealmString> getArtists() {
        return artists;
    }

    public void setArtists(RealmList<RealmString> artists) {
        this.artists = artists;
    }

    public RealmList<Tag> getTags() {
        return tags;
    }

    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
    }


}
