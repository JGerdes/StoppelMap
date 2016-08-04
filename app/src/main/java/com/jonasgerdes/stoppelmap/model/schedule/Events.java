package com.jonasgerdes.stoppelmap.model.schedule;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Jonas on 04.08.2016.
 */
public class Events extends RealmObject {

    private RealmList<Event> events;

    public RealmList<Event> getEvents() {
        return events;
    }

    public void setEvents(RealmList<Event> events) {
        this.events = events;
    }
}
