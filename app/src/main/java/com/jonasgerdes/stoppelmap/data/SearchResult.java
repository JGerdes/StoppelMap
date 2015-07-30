package com.jonasgerdes.stoppelmap.data;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jonasgerdes.stoppelmap.data.entity.Entity;

import java.util.List;
import java.util.Vector;

public class SearchResult {
    private String title;
    private List<Entity> entities;

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

    public List<Entity> getEntities() {
        return entities;
    }

    public void placeRelevantMarker(GoogleMap map){
        for(Entity e : entities){
            e.addMarkerTo(map);
        }
    }

    public CameraUpdate getCameraUpdate(LatLng currentPosition){
        LatLngBounds.Builder bounds = LatLngBounds.builder();
        for(Entity e : entities){
            bounds.include(e.position.latLng());
        }
        return CameraUpdateFactory.newLatLngBounds(bounds.build(), 120);
    }
}
