package com.jonasgerdes.stoppelmap.data;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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

public class DataController {

    private static final String DATA_PATH = "data/entities.json";
    private static final Map<String, Integer> LABEL_MAP;
    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("food", R.drawable.icon_dining);
        map.put("wc", R.drawable.icon_toilet);
        map.put("beer", R.drawable.icon_beer);
        map.put("attraction", R.drawable.icon_attraction);
        LABEL_MAP = Collections.unmodifiableMap(map);
    }


    private AssetManager assets;
    private List<Entity> entities;

    public DataController(AssetManager assets){
        this.assets = assets;
    }

    public void readData(){
        Gson reader = new GsonBuilder().create();
        //TODO: check null
        String json = readFileAsString(DATA_PATH);
        //TODO: catch exception?
        entities = reader.fromJson(json, EntityHolder.class).all();
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
            e.markerOptions = new MarkerOptions()
                    .position(e.position.latLng())
                    .icon(BitmapDescriptorFactory.fromBitmap(icon));
        }
    }

    private Bitmap createLabel(LayoutInflater inflater, String title, String[] labelIcons){
        LinearLayout tv = (LinearLayout) inflater.inflate(R.layout.marker_default, null, false);
        ((TextView)tv.findViewById(R.id.title)).setText(title);

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
        for(Entity e : entities){
            if(map.getProjection().getVisibleRegion().latLngBounds.contains(e.position.latLng())){
                if(e.currentMarker == null){
                    e.currentMarker = map.addMarker(e.markerOptions);
                }
            }else{
                if(e.currentMarker != null){
                    e.currentMarker.remove();
                    e.currentMarker = null;
                }
            }
        }
    }

    public List<Entity> getEntites(){
        return entities;
    }
}
