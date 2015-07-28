package com.jonasgerdes.stoppelmap.data.entity;


import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Entity {
    public String title;
    public Position position;
    public float minZoom;
    public String[] labels;
    public String[] tags;

    public MarkerOptions markerOptions;
    public Marker currentMarker;
}
