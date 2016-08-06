package com.jonasgerdes.stoppelmap.usecases.schedule.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.Tag;
import com.jonasgerdes.stoppelmap.model.schedule.Event;
import com.jonasgerdes.stoppelmap.model.schedule.search.ScheduleSearchResult;
import com.jonasgerdes.stoppelmap.model.shared.RealmString;
import com.jonasgerdes.stoppelmap.views.SearchCardView;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Jonas on 06.08.2016.
 */
public class ScheduleSearchAdapter extends SearchCardView.ResultAdapter<ScheduleSearchResultHolder> {

    public interface OnResultSelectedListener {
        void onResultSelected(ScheduleSearchResult result);
    }

    private List<ScheduleSearchResult> mSearchResults;
    private List<Event> mEvents;
    private OnResultSelectedListener mSelectedListener;

    public ScheduleSearchAdapter(RealmResults<Event> events) {
        mEvents = events;
        mSearchResults = new ArrayList<>();
        events.addChangeListener(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(RealmResults<Event> element) {
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onQueryChanged(String query) {
        List<ScheduleSearchResult> tempResults = new ArrayList<>();

        query = query.toLowerCase().trim();
        if (!query.isEmpty()) {
            for (Event event : mEvents) {
                //artist
                if (event.getArtists() != null && event.getArtists().size() > 0) {
                    boolean match = false;
                    for (RealmString artist : event.getArtists()) {
                        if (artist.getVal().toLowerCase().contains(query)) {
                            tempResults.add(
                                    new ScheduleSearchResult(
                                            event,
                                            ScheduleSearchResult.REASON_ARTIST,
                                            artist.getVal())
                            );
                            match = true;
                            break;
                        }
                    }
                    if (match) {
                        continue;
                    }
                }

                //tag/music search
                if (event.getTags() != null && event.getTags().size() > 0) {
                    boolean match = false;
                    for (Tag tag : event.getTags()) {
                        if (tag.getName().toLowerCase().contains(query)) {
                            tempResults.add(
                                    new ScheduleSearchResult(
                                            event,
                                            ScheduleSearchResult.REASON_TAG,
                                            tag.getName())
                            );
                            match = true;
                            break;
                        }
                    }
                    if (match) {
                        continue;
                    }
                }

                //name search
                if (event.getName().toLowerCase().contains(query)) {
                    tempResults.add(
                            new ScheduleSearchResult(
                                    event,
                                    ScheduleSearchResult.REASON_NAME,
                                    event.getName())
                    );
                    continue;
                }

            }
        }

        animateTo(tempResults);
    }

    @Override
    public ScheduleSearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_search_result_item, parent, false);
        return new ScheduleSearchResultHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleSearchResultHolder holder, int position) {
        final ScheduleSearchResult result = mSearchResults.get(position);
        holder.onBind(result);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedListener != null) {
                    mSelectedListener.onResultSelected(result);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchResults.size();
    }

    public void setSelectedListener(OnResultSelectedListener selectedListener) {
        mSelectedListener = selectedListener;
    }


    public void animateTo(List<ScheduleSearchResult> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    public ScheduleSearchResult removeItem(int position) {
        final ScheduleSearchResult model = mSearchResults.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, ScheduleSearchResult model) {
        mSearchResults.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ScheduleSearchResult model = mSearchResults.remove(fromPosition);
        mSearchResults.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    private void applyAndAnimateRemovals(List<ScheduleSearchResult> newModels) {
        for (int i = mSearchResults.size() - 1; i >= 0; i--) {
            final ScheduleSearchResult model = mSearchResults.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ScheduleSearchResult> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ScheduleSearchResult model = newModels.get(i);
            if (!mSearchResults.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ScheduleSearchResult> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final ScheduleSearchResult model = newModels.get(toPosition);
            final int fromPosition = mSearchResults.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
