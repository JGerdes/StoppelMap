package com.jonasgerdes.stoppelmap;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.jonasgerdes.stoppelmap.model.InitialTransaction;
import com.jonasgerdes.stoppelmap.versioning.RealmUpdateManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Jonas on 03.07.2016.
 */
public class StoppelMapApp extends Application {
    private static final String TAG = "StoppelMapApp";
    private static final int PERMISSION_REQUEST_CODE = 952;

    private Typeface mMainType;
    private Realm mRealm;
    private LocationManager mLocationManager;
    private List<Runnable> mPendingLocationRunnables;

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

        mPendingLocationRunnables = new ArrayList<>();
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

    public interface LocationRunnable {
        void run(Location location);
    }

    public void executeWithLocation(Activity activity, final LocationRunnable runnable) {

        Runnable onLocationSuccess = new Runnable() {
            @SuppressWarnings("MissingPermission")
            @Override
            public void run() {
                if (mLocationManager == null) {
                    mLocationManager
                            = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                }
                Location loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                runnable.run(loc);

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                onLocationSuccess.run();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, PERMISSION_REQUEST_CODE);
                mPendingLocationRunnables.add(onLocationSuccess);

            }
        } else {
            onLocationSuccess.run();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                }
            }

            if (mPendingLocationRunnables != null) {
                if (allGranted) {
                    for (Runnable pendingLocationRunnable : mPendingLocationRunnables) {
                        pendingLocationRunnable.run();
                    }
                }
                mPendingLocationRunnables.clear();
            }
        }
    }

    public LatLng getLastPosition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return getLastPositionChecked();
            } else {
                return null;
            }
        } else {
            return getLastPositionChecked();
        }
    }

    @SuppressWarnings("MissingPermission")
    private LatLng getLastPositionChecked() {
        if (mLocationManager == null) {
            mLocationManager
                    = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        Location loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc != null) {
            return new LatLng(loc.getLatitude(), loc.getLongitude());
        }
        return null;

    }
}
