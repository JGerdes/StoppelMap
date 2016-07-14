package com.jonasgerdes.stoppelmap.model;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonasgerdes.stoppelmap.model.transportation.Route;
import com.jonasgerdes.stoppelmap.model.transportation.Transportation;
import com.jonasgerdes.stoppelmap.util.FileUtil;

import io.realm.Realm;

/**
 * Created by Jonas on 08.07.2016.
 */
public class InitialTransaction implements Realm.Transaction {
    private static final String TAG = "InitialTransaction";
    
    private AssetManager mAssets;

    public InitialTransaction(AssetManager assets) {

        mAssets = assets;
    }

    @Override
    public void execute(Realm realm) {
        Log.d(TAG, "execute: start");
        String transportationJson = FileUtil.readAssetAsString(mAssets, "data/transportation.json");
        Log.d(TAG, "execute: read json successfully");
        if (transportationJson != null) {
            Log.d(TAG, "execute: json is not null");
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            Log.d(TAG, "execute: build gson");
            Transportation data = gson.fromJson(transportationJson, Transportation.class);
            Log.d(TAG, "execute: transformed json to pojo");
            for (Route route : data.routes) {
                realm.copyToRealm(route);
            }
            Log.d(TAG, "execute: wrote everything in realm");

        } else {
            Log.e(TAG, "execute: error reading asset to realm");
        }

    }

}
