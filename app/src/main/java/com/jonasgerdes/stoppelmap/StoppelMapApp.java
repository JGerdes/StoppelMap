package com.jonasgerdes.stoppelmap;

import android.app.Application;
import android.graphics.Typeface;

/**
 * Created by Jonas on 03.07.2016.
 */
public class StoppelMapApp extends Application {

    private Typeface mMainType;

    @Override
    public void onCreate() {
        super.onCreate();
        mMainType = Typeface.createFromAsset(getAssets(), "font/Damion-Regular.ttf");
    }

    public Typeface getMainTypeface() {
        return mMainType;
    }
}
