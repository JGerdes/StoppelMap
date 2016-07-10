package com.jonasgerdes.stoppelmap.usecases.transportation.route_detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.transportation.Station;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 10.07.2016.
 */
public class StationListAdapter extends RecyclerView.Adapter<StationHolder> {

    private List<Station> mStations;

    public StationListAdapter() {
        mStations = new ArrayList<>();
    }

    @Override
    public StationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transportation_stations_list_item, parent, false);
        return new StationHolder(view);
    }

    @Override
    public void onBindViewHolder(StationHolder holder, int position) {
        Station station = mStations.get(position);
        holder.onBind(station);
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }

    public void setStations(List<Station> stations) {
        mStations.clear();
        mStations.addAll(stations);
    }
}
