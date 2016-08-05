package com.jonasgerdes.stoppelmap.usecases.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.schedule.Event;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Jonas on 04.08.2016.
 */
public class EventAdapter extends RecyclerView.Adapter<EventHolder> {

    interface EventActionListener {
        void onFacebookAction(String url);

        void onLocationAction(MapEntity location);
    }

    private List<Event> mEvents;
    private boolean mHideLocationButton;
    private EventActionListener mActionListener;

    public EventAdapter(RealmResults<Event> events) {
        mEvents = events;
        events.addChangeListener(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(RealmResults<Event> element) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_event_list_item, parent, false);
        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        final Event event = mEvents.get(position);
        holder.onBind(event);
        holder.mFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionListener != null) {
                    mActionListener.onFacebookAction(event.getFacebookUrl());
                }
            }
        });
        holder.mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionListener != null) {
                    mActionListener.onLocationAction(event.getLocation());
                }
            }
        });
        if (mHideLocationButton) {
            holder.mLocationButton.setVisibility(View.GONE);
            holder.mLocations.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public void setActionListener(EventActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void setHideLocationButton(boolean hideLocationButton) {
        mHideLocationButton = hideLocationButton;
    }
}
