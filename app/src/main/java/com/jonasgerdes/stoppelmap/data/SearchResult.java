package com.jonasgerdes.stoppelmap.data;


import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jonasgerdes.stoppelmap.MainActivity;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.data.entity.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class SearchResult {
    private String title;
    private List<Entity> entities;
    private int iconDrawable;

    public SearchResult(String title, List<Entity> entities) {
        this.title = title;
        this.entities = entities;
    }

    public SearchResult(String title, Entity e) {
        this.title = title;
        this.entities = new Vector<>();
        entities.add(e);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getIconDrawable(){
        return iconDrawable;
    }

    public void setIconDrawable(int iconDrawable){
        this.iconDrawable = iconDrawable;
    }
    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity e){
        entities.add(e);
    }

    public void placeRelevantMarker(GoogleMap map){
        for(Entity e : entities){
            e.addMarkerTo(map);
        }
    }

    public CameraUpdate getCameraUpdate(LatLng currentPosition){
        LatLngBounds.Builder bounds = LatLngBounds.builder();
        List<Entity> relevantEntities;
        if(currentPosition != null && entities.size() > 5){
            relevantEntities = new Vector();
            sortEntities(currentPosition);
            for(int i=0; i<6; i++){
                relevantEntities.add(entities.get(i));
            }
        }else{
            relevantEntities = entities;
        }
        for(Entity e :relevantEntities){
            bounds.include(e.position.latLng());
        }
        if(currentPosition != null){
            bounds.include(currentPosition);
        }
        return CameraUpdateFactory.newLatLngBounds(bounds.build(), MainActivity.convertDpToPixel(64));
    }

    private void sortEntities(final LatLng pos) {
        Collections.sort(entities, new Comparator<Entity>() {
            @Override
            public int compare(Entity lhs, Entity rhs) {
                float[] dist1 = new float[1];
                Location.distanceBetween(pos.latitude, pos.longitude,
                                        lhs.position.lat, lhs.position.lon, dist1);
                float[] dist2 = new float[1];
                Location.distanceBetween(pos.latitude, pos.longitude,
                        rhs.position.lat, rhs.position.lon, dist2);

                if(dist1[0] < dist2[0]){
                    return -1;
                }else{
                    return 1;
                }

            }
        });
    }
}
