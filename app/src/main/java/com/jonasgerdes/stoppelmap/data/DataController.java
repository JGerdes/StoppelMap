package com.jonasgerdes.stoppelmap.data;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.data.entity.Entity;
import com.jonasgerdes.stoppelmap.data.entity.EntityHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class DataController {

    private static final String PATH_HOUSES_PARKING = "data/houses_parking.json";
    private static final String PATH_ATTRACTIONS = "data/attractions.json";
    private static final String PATH_TENTS = "data/tents.json";
    private static final String PATH_STALLS = "data/stalls.json";
    private static final String PATH_MISC = "data/misc.json";
    private static final Map<String, Integer> LABEL_MAP;
    private static final List<String> IGNORED_TITLES;
    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("food", R.drawable.icon_dining);
        map.put("wc", R.drawable.icon_toilet_12);
        map.put("beer", R.drawable.icon_beer_12);
        map.put("attraction", R.drawable.icon_attraction_12);
        map.put("parking", R.drawable.icon_parking_12);
        map.put("train", R.drawable.icon_train_12);
        map.put("bike", R.drawable.icon_bike_12);
        map.put("taxi", R.drawable.icon_taxi_12);
        map.put("bus", R.drawable.icon_bus_12);
        map.put("doctor", R.drawable.icon_doctor_12);
        map.put("info", R.drawable.icon_info_12);
        map.put("atm", R.drawable.icon_atm_12);
        map.put("tent", R.drawable.icon_tent_12);
        map.put("candy", R.drawable.icon_candy_12);

        LABEL_MAP = Collections.unmodifiableMap(map);



        List<String> ignore = new Vector<>();
        ignore.add("WC");
        ignore.add("Süßwaren");
        ignore.add("Crêpes");
        ignore.add("Mais");
        ignore.add("Blumenkohl");
        ignore.add("Imbiss");
        ignore.add("Pommes");
        ignore.add("Pilze");
        ignore.add("Brezel");
        ignore.add("Pizza");
        ignore.add("Hotdog");
        ignore.add("Bratwurst");
        ignore.add("Poffertje");
        ignore.add("Reibekuchen");
        ignore.add("Chinesisch");
        ignore.add("Pasta");
        ignore.add("Fisch");
        ignore.add("Lachs");
        ignore.add("Käse");
        ignore.add("Gyros");
        ignore.add("Flammkuchen");
        ignore.add("Schinken");
        ignore.add("Grillschinken");
        ignore.add("Spießbraten");
        ignore.add("Spieße");
        ignore.add("Slush");
        ignore.add("Eis");
        ignore.add("Gurken");
        ignore.add("Frozen Yoghurt");
        ignore.add("Schoko Früchte");

        IGNORED_TITLES = ignore;


    }

    private static final Map<String, Integer> TAG_ICONS;
    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("Fahrgeschäft", R.drawable.icon_attraction_24);
        map.put("Attraktion", R.drawable.icon_attraction_24);
        map.put("Zelt", R.drawable.icon_tent_24);
        map.put("Essen", R.drawable.icon_dining_24);
        map.put("Parkplatz", R.drawable.icon_parking_24);
        map.put("Zug", R.drawable.icon_train_24);
        map.put("NordWestBahn", R.drawable.icon_train_24);
        map.put("Bus", R.drawable.icon_bus_24);
        map.put("Info", R.drawable.icon_info_24);
        map.put("Geldautomat", R.drawable.icon_atm_24);
        map.put("Arzt", R.drawable.icon_doctor_24);
        map.put("Rotes Kreuz", R.drawable.icon_doctor_24);
        map.put("Toilette", R.drawable.icon_toilet_24);
        map.put("WC", R.drawable.icon_toilet_24);
        map.put("Klo", R.drawable.icon_toilet_24);

        //Essen
        map.put("Pommes", R.drawable.icon_dining_24);
        map.put("Pizza", R.drawable.icon_dining_24);
        map.put("Brezel", R.drawable.icon_dining_24);
        map.put("Hamburger", R.drawable.icon_dining_24);
        map.put("HotDog", R.drawable.icon_dining_24);
        map.put("Bratwurst", R.drawable.icon_dining_24);
        map.put("Currywurst", R.drawable.icon_dining_24);
        map.put("Bratkartoffeln", R.drawable.icon_dining_24);
        map.put("Spiegeleier", R.drawable.icon_dining_24);
        map.put("Döner", R.drawable.icon_dining_24);
        map.put("Champions", R.drawable.icon_dining_24);
        map.put("Crépes", R.drawable.icon_dining_24);
        map.put("Eis", R.drawable.icon_dining_24);


        //Getr�nke
        map.put("Bier", R.drawable.icon_beer_24);

        TAG_ICONS = Collections.unmodifiableMap(map);
    }


    private AssetManager assets;
    private List<Entity> entities;

    public DataController(AssetManager assets){
        this.assets = assets;
    }

    public void readData(){
        Gson reader = new GsonBuilder().create();
        //TODO: check null
        String json = readFileAsString(PATH_HOUSES_PARKING);
        //TODO: catch exception?
        EntityHolder data = reader.fromJson(json, EntityHolder.class);
        data.attractions = reader.fromJson(readFileAsString(PATH_ATTRACTIONS), EntityHolder.class).attractions;
        data.tents = reader.fromJson(readFileAsString(PATH_TENTS), EntityHolder.class).tents;
        data.stalls = reader.fromJson(readFileAsString(PATH_STALLS), EntityHolder.class).stalls;
        data.misc = reader.fromJson(readFileAsString(PATH_MISC), EntityHolder.class).misc;

        entities = data.all();
    }


    public String readFileAsString(String path){
        try {
            InputStream is = assets.open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void createLabels(LayoutInflater inflater){
        for(Entity e : entities){
            Bitmap icon = createLabel(inflater, e.title,e.labels);
            int w = icon.getWidth(),
                h = icon.getHeight();
            e.markerOptions = new MarkerOptions()
                    .position(e.position.latLng())
                    .icon(BitmapDescriptorFactory.fromBitmap(icon));
            e.markerOptions.anchor(0.5f, 0.5f);
        }
    }

    private Bitmap createLabel(LayoutInflater inflater, String title, String[] labelIcons){
        LinearLayout tv = (LinearLayout) inflater.inflate(R.layout.marker_default, null, false);
        ((TextView)tv.findViewById(R.id.title)).setText(Html.fromHtml(title));

        LinearLayout icons = (LinearLayout)tv.findViewById(R.id.icons);
        for(String icon : labelIcons){
            ImageView view = new ImageView(inflater.getContext());
            view.setImageResource(LABEL_MAP.get(icon));
            icons.addView(view);
        }

        tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        tv.setDrawingCacheEnabled(true);
        tv.buildDrawingCache();
        return tv.getDrawingCache();
    }

    public void placeRelevantMarkers(GoogleMap map){
        LatLngBounds originalBounds = map.getProjection().getVisibleRegion().latLngBounds;
        //make bounds slightly bigger so we don't see label dis-/appear
        LatLngBounds bounds = new LatLngBounds(
                new LatLng(
                        originalBounds.southwest.latitude - 0.0001,
                        originalBounds.southwest.longitude - 0.0001
                ),
                new LatLng(
                        originalBounds.northeast.latitude + 0.0001,
                        originalBounds.northeast.longitude + 0.0001
                ));

        float zoom = map.getCameraPosition().zoom;
        int debugCount = 0;
        for(Entity e : entities){
            //bounds.contains(e.position.latLng())
            if(zoom >= e.minZoom && (e.maxZoom == 0 || zoom <= e.maxZoom)){
                debugCount++;
                e.addMarkerTo(map);
            }else{
                e.removeMarkerFrom(map);
            }
        }
        Log.d("data", "#new Entities: " + debugCount);
    }

    public void removeAllMarkers(GoogleMap map){
        for(Entity e : entities){
            e.removeMarkerFrom(map);
        }
    }

    public List<Entity> getEntites(){
        return entities;
    }

    public List<SearchResult> query(String query){
        List<SearchResult> result = new Vector<>();
        HashMap<String, SearchResult> tags = new HashMap<>();
        for(Entity e : entities){
            if(!IGNORED_TITLES.contains(e.title) && e.title.toLowerCase().contains(query.toLowerCase())){
                SearchResult entityResult = new SearchResult(e.title, e);
                for(String tag : e.tags){
                    if(TAG_ICONS.containsKey(tag)){
                        entityResult.setIconDrawable(TAG_ICONS.get(tag));
                        break;
                    }
                }
                result.add(entityResult);
            }
            for(String tag : e.tags){
                if(tag.toLowerCase().contains(query.toLowerCase())) {
                    if (tags.containsKey(tag)) {
                        tags.get(tag).addEntity(e);
                    } else {
                        tags.put(tag, new SearchResult(tag, e));
                    }
                }
            }
        }
        for(String tag : tags.keySet()){
            SearchResult tagResult = tags.get(tag);
            if(TAG_ICONS.containsKey(tagResult.getTitle())){
                tagResult.setIconDrawable(TAG_ICONS.get(tagResult.getTitle()));
            }
            tagResult.setTitle("#"+tagResult.getTitle());
            result.add(tagResult);
        }
        return result;
    }
}
