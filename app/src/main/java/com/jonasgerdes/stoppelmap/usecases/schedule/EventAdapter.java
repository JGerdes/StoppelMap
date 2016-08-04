package com.jonasgerdes.stoppelmap.usecases.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.schedule.Event;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Jonas on 04.08.2016.
 */
public class EventAdapter extends RecyclerView.Adapter<EventHolder> {

    private List<Event> mEvents;

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
        Event event = mEvents.get(position);
        holder.onBind(event);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}
