package com.jonasgerdes.stoppelmap.model.map;

import com.google.android.gms.maps.model.MarkerOptions;
import com.jonasgerdes.stoppelmap.model.shared.GeoLocation;
import com.jonasgerdes.stoppelmap.model.shared.RealmString;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonas on 18.07.2016.
 */
public class MapEntity extends RealmObject {

    @PrimaryKey
    private String uuid;
    private int type;

    private String name;
    private RealmList<GeoLocation> bounds;
    private GeoLocation origin;
    private String headerImageFile;
    private RealmList<RealmString> icons;
    private Info info;
    private RealmList<Tag> tags;

    @Ignore
    MarkerOptions mMarkerOptions;

    public MapEntity() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<GeoLocation> getBounds() {
        return bounds;
    }

    public void setBounds(RealmList<GeoLocation> bounds) {
        this.bounds = bounds;
    }

    public GeoLocation getOrigin() {
        return origin;
    }

    public void setOrigin(GeoLocation origin) {
        this.origin = origin;
    }

    public String getHeaderImageFile() {
        return headerImageFile;
    }

    public void setHeaderImageFile(String headerImageFile) {
        this.headerImageFile = headerImageFile;
    }

    public RealmList<RealmString> getIcons() {
        return icons;
    }

    public void setIcons(RealmList<RealmString> icons) {
        this.icons = icons;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public MarkerOptions getMarkerOptions() {
        return mMarkerOptions;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        mMarkerOptions = markerOptions;
    }

    public RealmList<Tag> getTags() {
        return tags;
    }

    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
    }
}
