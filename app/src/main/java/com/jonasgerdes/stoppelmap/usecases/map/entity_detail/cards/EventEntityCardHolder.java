package com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.schedule.Event;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCard;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCardHolder;
import com.jonasgerdes.stoppelmap.usecases.schedule.EntityScheduleActivity;
import com.jonasgerdes.stoppelmap.usecases.schedule.EventHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 19.07.2016.
 */
public class EventEntityCardHolder extends EntityCardHolder {

    public static final int LAYOUT = R.layout.map_entity_card_event;

    @BindView(R.id.details)
    Button mDetailButton;

    private EventHolder mEventHolder;

    public EventEntityCardHolder(View itemView) {
        super(itemView);
        mEventHolder = new EventHolder(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(final EntityCard entityCard) {
        final Context context = itemView.getContext();
        Event event = entityCard.getExtraData();
        boolean showDayInStart = true;
        mEventHolder.bindContent(event, showDayInStart);
        mEventHolder.bindLocation(null);
        mEventHolder.bindFacebookLink(event.getFacebookUrl());

        mDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        EntityScheduleActivity.createIntent(context, entityCard.getMapEntity());
                context.startActivity(intent);
            }
        });
    }
}
