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
public class StationListAdapter extends RecyclerView.Adapter<AbstractStationHolder> {

    public interface StationSelectedListener {
        void onStationSelected(Station station);
    }

    private static final int TYPE_DESTINATION = 0;
    private static final int TYPE_DEFAULT_NODE = 1;
    private List<Station> mStations;
    private StationSelectedListener mStationSelectedListener;

    public StationListAdapter() {
        mStations = new ArrayList<>();
    }

    @Override
    public AbstractStationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_DESTINATION:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.transportation_stations_list_item_destination, parent, false);
                return new DestinationHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.transportation_stations_list_item, parent, false);
                return new StationHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_DESTINATION;
        } else {
            return TYPE_DEFAULT_NODE;
        }
    }

    @Override
    public void onBindViewHolder(AbstractStationHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_DESTINATION:
                holder.onBind(position != 0, false);
                break;
            default:
                final Station station = mStations.get(position);
                ((StationHolder) holder).onBind(station, position != 0, true);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStationSelectedListener != null) {
                            mStationSelectedListener.onStationSelected(station);
                        }
                    }
                });
                ((StationHolder) holder).mDetailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStationSelectedListener != null) {
                            mStationSelectedListener.onStationSelected(station);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mStations.size() + 1;
    }

    public void setStations(List<Station> stations) {
        mStations.clear();
        mStations.addAll(stations);
    }

    public void setStationSelectedListener(StationSelectedListener stationSelectedListener) {
        mStationSelectedListener = stationSelectedListener;
    }
}
