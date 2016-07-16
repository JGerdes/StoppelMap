package com.jonasgerdes.stoppelmap.usecases.transportation.station_detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.transportation.Departure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 16.07.2016.
 */
public class DepatureAdapter extends RecyclerView.Adapter<DepartureHolder> {

    private List<Departure> mDepartures;

    public DepatureAdapter() {
        mDepartures = new ArrayList<>();
    }

    @Override
    public DepartureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transportation_depatures_list_item, parent, false);
        return new DepartureHolder(view);
    }

    @Override
    public void onBindViewHolder(DepartureHolder holder, int position) {
        Departure departure = mDepartures.get(position);
        boolean isNext = false;
        holder.onBind(departure, isNext);
    }

    @Override
    public int getItemCount() {
        return mDepartures.size();
    }

    public void setDepartures(List<Departure> departures) {
        mDepartures = departures;
        notifyDataSetChanged();
    }
}
