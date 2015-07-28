package com.jonasgerdes.stoppelmap.data.entity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class EntityHolder {
    public Entity[] attractions;
    public Entity[] houses;

    public List<Entity> all(){
        List<Entity> all = new Vector<>();
        all.addAll(Arrays.asList(attractions));
        all.addAll(Arrays.asList(houses));
        return all;
    }

}
