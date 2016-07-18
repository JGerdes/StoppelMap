package com.jonasgerdes.stoppelmap.model;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonasgerdes.stoppelmap.model.map.MapEntities;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.transportation.Route;
import com.jonasgerdes.stoppelmap.model.transportation.Transportation;
import com.jonasgerdes.stoppelmap.util.FileUtil;

import io.realm.Realm;

/**
 * Created by Jonas on 08.07.2016.
 */
public class InitialTransaction implements Realm.Transaction {
    private static final String TAG = "InitialTransaction";

    private static final String[] MAP_FILES = new String[]{
            "map/misc"
    };

    private AssetManager mAssets;

    public InitialTransaction(AssetManager assets) {

        mAssets = assets;
    }

    @Override
    public void execute(Realm realm) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();


        Transportation transp = readJsonFile(gson, "transportation", Transportation.class);
        if (transp != null) {
            for (Route route : transp.routes) {
                realm.copyToRealm(route);
            }
        } else {
            Log.e(TAG, "execute: error reading asset to realm");
        }

        for (String file : MAP_FILES) {
            MapEntities entities = readJsonFile(gson, file, MapEntities.class);
            if (entities != null) {
                for (MapEntity mapEntity : entities.entities) {
                    realm.copyToRealm(mapEntity);
                }
            }
        }


    }

    private <T> T readJsonFile(Gson gson, String name, Class<T> classOfT) {
        String json = FileUtil.readAssetAsString(mAssets, "data/" + name + ".json");
        if (json != null) {
            T data = gson.fromJson(json, classOfT);
            return data;
        }
        return null;
    }

}
