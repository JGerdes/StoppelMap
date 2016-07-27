package com.jonasgerdes.stoppelmap.model.map.search;

import android.support.annotation.LayoutRes;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jonas on 22.07.2016.
 */
public abstract class SearchResult {

    public final static int DEFAULT_SEARCH_ZOOM = 18;

    protected float mMatchingFactor = 1;

    public abstract CameraUpdate getCameraUpdate();

    public abstract CameraUpdate getCameraUpdate(LatLng userLocation);

    public float getMatchingFactor() {
        return mMatchingFactor;
    }

    public void setMatchingFactor(float matchingFactor) {
        mMatchingFactor = matchingFactor;
    }

    public abstract
    @LayoutRes
    int getLayout();
}
