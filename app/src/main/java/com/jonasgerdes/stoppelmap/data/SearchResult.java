package com.jonasgerdes.stoppelmap.data;


import com.jonasgerdes.stoppelmap.data.entity.Entity;

public class SearchResult {
    private String title;
    private Entity entity;

    public SearchResult(String title, Entity entity) {
        this.title = title;
        this.entity = entity;
    }

    public String getTitle() {
        return title;
    }

    public Entity getEntity() {
        return entity;
    }
}
