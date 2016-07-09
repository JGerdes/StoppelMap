package com.jonasgerdes.stoppelmap;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;

import com.jonasgerdes.stoppelmap.model.InitialTransaction;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Jonas on 03.07.2016.
 */
public class StoppelMapApp extends Application {

    private Typeface mMainType;
    private Realm mRealm;

    @Override
    public void onCreate() {
        super.onCreate();
        mMainType = Typeface.createFromAsset(getAssets(), "font/Damion-Regular.ttf");
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .initialData(new InitialTransaction())
                .build();
        mRealm = Realm.getInstance(config);
    }

    public Typeface getMainTypeface() {
        return mMainType;
    }

    public Realm getRealm() {
        return mRealm;
    }

    public static StoppelMapApp getViaActivity(Activity activity) {
        return (StoppelMapApp) activity.getApplication();
    }
}
