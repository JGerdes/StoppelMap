package com.jonasgerdes.stoppelmap.model.map.search;

import android.support.annotation.LayoutRes;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;

/**
 * Created by Jonas on 22.07.2016.
 */
public abstract class SearchResult {

    public final static int DEFAULT_SEARCH_ZOOM = 18;

    protected float mMatchingFactor = 1;

    public abstract CameraUpdate getCameraUpdate();

    public abstract CameraUpdate getCameraUpdate(LatLng userLocation);

    public abstract boolean containsEntity(MapEntity entity);

    public float getMatchingFactor() {
        return mMatchingFactor;
    }

    public void setMatchingFactor(float matchingFactor) {
        mMatchingFactor = matchingFactor;
    }

    public abstract String getIdentifier();

    @Override
    public boolean equals(Object o) {
        if (o instanceof SearchResult) {
            return ((SearchResult) o).getIdentifier().equals(this.getIdentifier());
        }
        return false;
    }

    public abstract
    @LayoutRes
    int getLayout();
}
