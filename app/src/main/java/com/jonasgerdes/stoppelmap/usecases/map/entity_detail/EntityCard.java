package com.jonasgerdes.stoppelmap.usecases.map.entity_detail;

import com.jonasgerdes.stoppelmap.model.map.MapEntity;

/**
 * Created by Jonas on 19.07.2016.
 */
public class EntityCard {
    private int type;
    private MapEntity mapEntity;

    public EntityCard(int type, MapEntity mapEntity) {
        this.type = type;
        this.mapEntity = mapEntity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MapEntity getMapEntity() {
        return mapEntity;
    }

    public void setMapEntity(MapEntity mapEntity) {
        this.mapEntity = mapEntity;
    }
}
