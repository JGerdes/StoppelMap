package com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards;

import android.view.View;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.schedule.Event;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCard;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCardHolder;
import com.jonasgerdes.stoppelmap.usecases.schedule.EventHolder;

import butterknife.ButterKnife;

/**
 * Created by Jonas on 19.07.2016.
 */
public class EventEntityCardHolder extends EntityCardHolder {

    public static final int LAYOUT = R.layout.map_entity_card_event;

    private EventHolder mEventHolder;

    public EventEntityCardHolder(View itemView) {
        super(itemView);
        mEventHolder = new EventHolder(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(EntityCard entityCard) {
        Event event = entityCard.getExtraData();
        mEventHolder.bindContent(event);
        mEventHolder.bindLocation(null);
        mEventHolder.bindFacebookLink(event.getFacebookUrl());
    }
}
