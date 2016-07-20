package com.jonasgerdes.stoppelmap;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.util.Log;

import com.jonasgerdes.stoppelmap.model.InitialTransaction;
import com.jonasgerdes.stoppelmap.versioning.RealmUpdateManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Jonas on 03.07.2016.
 */
public class StoppelMapApp extends Application {
    private static final String TAG = "StoppelMapApp";

    private Typeface mMainType;
    private Realm mRealm;

    @Override
    public void onCreate() {
        super.onCreate();
        RealmUpdateManager updateManager = new RealmUpdateManager(this);
        if (!updateManager.isUptodate()) {
            Log.d(TAG, "start realm update...");
            long t = System.currentTimeMillis();
            updateManager.update();
            t = System.currentTimeMillis() - t;
            Log.d(TAG, "realm update took " + t + "ms");
        }

        long t = System.currentTimeMillis();
        RealmConfiguration realmConfig;
        if (getResources().getBoolean(R.bool.is_debug)) {
            realmConfig = new RealmConfiguration.Builder(this)
                    .deleteRealmIfMigrationNeeded()
                    .initialData(new InitialTransaction(getAssets()))
                    .build();
        } else {
            Log.d(TAG, "init realmdb");
            String realmFilePath = getResources()
                    .getString(R.string.versioning_internal_realm_file_path);
            realmConfig = new RealmConfiguration.Builder(this)
                    .name(realmFilePath)
                    .build();
        }
        mRealm = Realm.getInstance(realmConfig);
        t = System.currentTimeMillis() - t;
        Log.d(TAG, "init realm took " + t + "ms");

        t = System.currentTimeMillis();
        Log.d(TAG, "loading font");
        mMainType = Typeface.createFromAsset(getAssets(), "font/Damion-Regular.ttf");
        t = System.currentTimeMillis() - t;
        Log.d(TAG, "took " + t + "ms");
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
