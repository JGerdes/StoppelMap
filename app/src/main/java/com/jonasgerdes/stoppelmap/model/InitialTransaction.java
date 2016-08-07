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
import com.jonasgerdes.stoppelmap.model.schedule.Event;
import com.jonasgerdes.stoppelmap.model.schedule.Events;
import com.jonasgerdes.stoppelmap.model.shared.RealmString;
import com.jonasgerdes.stoppelmap.model.transportation.Route;
import com.jonasgerdes.stoppelmap.model.transportation.Transportation;
import com.jonasgerdes.stoppelmap.util.FileUtil;
import com.jonasgerdes.stoppelmap.util.StringUtil;

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


        Transportation transp = readJsonFile(gson, "data/transportation.json", Transportation.class);
        if (transp != null) {
            for (Route route : transp.routes) {
                realm.copyToRealm(route);
            }
        } else {
            Log.e(TAG, "execute: error reading asset to realm");
        }


        String[] files = new String[0];
        try {
            files = mAssets.list("data/map");
            for (String file : files) {
                Log.d(TAG, "execute: " + file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String file : files) {
            MapEntities entities = readJsonFile(gson, "data/map/" + file, MapEntities.class);
            if (entities != null) {
                for (MapEntity mapEntity : entities.entities) {
                    checkForNull(mapEntity, mapEntity.getName(), "Name");
                    checkForNull(mapEntity, mapEntity.getBounds(), "Bounds");
                    checkForNull(mapEntity, mapEntity.getOrigin(), "Origin");
                    checkForNull(mapEntity, mapEntity.getType(), "Type");
                    checkForNull(mapEntity, mapEntity.getTags(), "Tags");
                    checkForNull(mapEntity, mapEntity.getIcons(), "Icons");


                    //add first coordinate as last to close polygon
                    if (mapEntity.getBounds() != null && mapEntity.getBounds().size() > 0) {
                        mapEntity.getBounds().add(mapEntity.getBounds().get(0));
                    }

                    realm.copyToRealm(mapEntity);
                }
            }
        }
        try {
            files = mAssets.list("data/schedule");
            for (String file : files) {
                Log.d(TAG, "execute: " + file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String file : files) {
            if (file.contains("template")) {
                continue;
            }
            Events events = readJsonFile(gson, "data/schedule/" + file, Events.class);
            if (events != null) {
                for (Event event : events.getEvents()) {
                    checkForNull(event, event.getUuid(), "uuid");
                    checkForNull(event, event.getName(), "name");
                    checkForNull(event, event.getStart(), "start");
                    if (event.getLocationUuid() != null) {
                        event.setLocation(
                                realm.where(MapEntity.class)
                                        .equalTo("uuid", event.getLocationUuid())
                                        .findFirst()
                        );
                    }

                    realm.copyToRealm(event);
                }
            }
        }
    }

    private void checkForNull(MapEntity entity, Object field, String fieldName) {
        if (field == null) {
            Log.e(TAG, "checkForNull: " + fieldName + " is/are null on" + entity.getUuid());
        }
    }

    private void checkForNull(Event event, Object field, String fieldName) {
        if (field == null) {
            Log.e(TAG, "checkForNull: " + fieldName + " is/are null on" + event.getUuid());
        }
    }

    private <T> T readJsonFile(Gson gson, String path, Class<T> classOfT) {
        Log.d(TAG, "readJsonFile: path = [" + path + "], class = [" + classOfT + "]");
        String json = FileUtil.readAssetAsString(mAssets, path);
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
            new AdvancedTag(R.drawable.ic_local_hospital_black_24dp, "DRK", "Rotes Kreuz", "Erste Hilfe"),
            new AdvancedTag("Polizei"),
            new AdvancedTag("Feuerwehr"),
            new AdvancedTag(R.drawable.ic_local_atm_black_24dp, "Geldautomat"),
            new AdvancedTag(R.drawable.ic_tent_black_24dp, "Zelt", "Festzelt"),

            new AdvancedTag(R.drawable.ic_directions_bus_black_24dp, "Busse",
                    StringUtil.getEmojiByUnicode(StringUtil.EMOJI_BUS)),

            new AdvancedTag(R.drawable.ic_directions_car_black_24dp, "Parkplatz"),
            new AdvancedTag(R.drawable.ic_local_taxi_black_24dp, "Taxi"),

            //Food
            new AdvancedTag(R.drawable.ic_local_dining_black_24dp, "Pommes", "Fritten"),
            new AdvancedTag(R.drawable.ic_local_dining_black_24dp, "Bratwurst"),
            new AdvancedTag(R.drawable.ic_local_dining_black_24dp, "Bratkartoffeln"),
            new AdvancedTag(R.drawable.ic_local_dining_black_24dp, "Spiegeleier"),
            new AdvancedTag(R.drawable.ic_local_dining_black_24dp, "Spießbraten"),
            new AdvancedTag(R.drawable.ic_local_dining_black_24dp, "Italiener"),

            //Drinks
            new AdvancedTag(R.drawable.ic_beer_black_24dp, "Bier"),
            new AdvancedTag(R.drawable.ic_local_cafe_black_24dp, "Kaffee", "Café"),
            new AdvancedTag(R.drawable.ic_local_bar_black_24dp, "Ausschank"),
            new AdvancedTag(R.drawable.ic_local_bar_black_24dp, "Cocktails"),

            //Rides
            new AdvancedTag(R.drawable.ic_fairground_black_24dp, "Fahrgeschäft"),
            new AdvancedTag(R.drawable.ic_fairground_black_24dp,
                    "Laufgeschäft", "FunHouse", "Spaßhaus"),
            new AdvancedTag(R.drawable.ic_fairground_black_24dp, "Autoskooter", "Autoscooter"),
            new AdvancedTag(R.drawable.ic_child_care_black_24dp, "Für Kinder"),
            new AdvancedTag(R.drawable.ic_fairground_black_24dp,
                    "Wasserbahn", "Wildwasserbahn", "Baumstammkanal"),
            new AdvancedTag(R.drawable.ic_fairground_black_24dp, "Kettenflieger"),
            new AdvancedTag(R.drawable.ic_fairground_black_24dp, "Kinderkarussell"),
            new AdvancedTag(R.drawable.ic_fairground_black_24dp, "Achterbahn",
                    StringUtil.getEmojiByUnicode(StringUtil.EMOJI_ROLLERCOASTER)),
            new AdvancedTag(R.drawable.ic_fairground_black_24dp, "GoKarts"),
            new AdvancedTag(R.drawable.ic_fairground_black_24dp, "Geisterbahn")
    };

    // @formatter: on
}
