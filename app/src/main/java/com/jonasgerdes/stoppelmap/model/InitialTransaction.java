package com.jonasgerdes.stoppelmap.model;

import android.content.res.AssetManager;
import android.support.annotation.DrawableRes;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.MapEntities;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.map.Tag;
import com.jonasgerdes.stoppelmap.model.shared.RealmString;
import com.jonasgerdes.stoppelmap.model.transportation.Route;
import com.jonasgerdes.stoppelmap.model.transportation.Transportation;
import com.jonasgerdes.stoppelmap.util.FileUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Jonas on 08.07.2016.
 */
public class InitialTransaction implements Realm.Transaction {
    private static final String TAG = "InitialTransaction";

    private static final String[] MAP_FILES = new String[]{
            "map/misc",
            "map/buildings"
    };

    private AssetManager mAssets;

    public InitialTransaction(AssetManager assets) {

        mAssets = assets;
    }

    @Override
    public void execute(Realm realm) {
        Type stringListToken = new TypeToken<RealmList<RealmString>>() {
        }.getType();
        Type tagListToken = new TypeToken<RealmList<Tag>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(stringListToken, new TypeAdapter<RealmList<RealmString>>() {

                    @Override
                    public void write(JsonWriter out, RealmList<RealmString> value) throws IOException {
                        // Ignore
                    }

                    @Override
                    public RealmList<RealmString> read(JsonReader in) throws IOException {
                        RealmList<RealmString> list = new RealmList<>();
                        in.beginArray();
                        while (in.hasNext()) {
                            list.add(new RealmString(in.nextString()));
                        }
                        in.endArray();
                        return list;
                    }
                })
                .registerTypeAdapter(tagListToken, new TypeAdapter<RealmList<Tag>>() {

                    @Override
                    public void write(JsonWriter out, RealmList<Tag> value) throws IOException {
                        // Ignore
                    }

                    @Override
                    public RealmList<Tag> read(JsonReader in) throws IOException {
                        RealmList<Tag> list = new RealmList<>();
                        in.beginArray();
                        while (in.hasNext()) {
                            String tagName = in.nextString();
                            List<Tag> tags = createTagsFromName(tagName);
                            if (tags != null) {
                                list.addAll(tags);
                            } else {
                                list.add(new Tag(tagName));
                            }
                        }
                        in.endArray();
                        return list;
                    }
                })
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


    private List<Tag> createTagsFromName(String name) {
        name = name.toLowerCase().trim();
        for (AdvancedTag tagSynonym : TAG_SYNONYMS) {
            boolean matches = false;
            for (String synonym : tagSynonym.synonyms) {
                if (name.equals(synonym.toLowerCase())) {
                    matches = true;
                    break;
                }
            }
            if (matches) {
                List<Tag> tags = new ArrayList<>(tagSynonym.synonyms.length);
                for (String synonym : tagSynonym.synonyms) {
                    Tag tag = new Tag(synonym);
                    tag.setIcon(tagSynonym.icon);
                    tags.add(tag);
                }
                return tags;
            }
        }
        return null;
    }

    private static class AdvancedTag {
        public String[] synonyms;
        public
        @DrawableRes
        int icon = Tag.ICON_NONE;

        public AdvancedTag(int icon, String... synonyms) {
            this.synonyms = synonyms;
            this.icon = icon;
        }

        public AdvancedTag(String... synonyms) {
            this.synonyms = synonyms;
        }
    }

    // @formatter: off
    public static AdvancedTag[] TAG_SYNONYMS = new AdvancedTag[]{
            //Misc
            new AdvancedTag(R.drawable.ic_wc_black_24dp, "WC", "Toilette", "Klo"),

            //Food
            new AdvancedTag(R.drawable.ic_local_dining_black_24dp, "Pommes", "Fries", "Fritten"),
            new AdvancedTag(R.drawable.ic_local_dining_black_24dp, "Bratwurst"),
            new AdvancedTag(R.drawable.ic_local_dining_black_24dp, "Bratkartoffeln"),
            new AdvancedTag(R.drawable.ic_local_dining_black_24dp, "Spiegeleier"),

            //Drinks
            new AdvancedTag(R.drawable.ic_beer_black_24dp, "Bier"),
    };

    // @formatter: on
}
