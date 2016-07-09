package com.jonasgerdes.stoppelmap.usecases.transportation.route_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.transportation.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 09.07.2016.
 */
public class RouteListAdapter extends RecyclerView.Adapter<RouteHolder> {

    private List<Route> mRoutes;

    public RouteListAdapter() {
        mRoutes = new ArrayList<>();
    }

    @Override
    public RouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transportation_routes_list_item, parent, false);
        return new RouteHolder(view);
    }

    @Override
    public void onBindViewHolder(RouteHolder holder, int position) {
        Route route = mRoutes.get(position);
        holder.onBind(route);
        if (holder.mGoogleMap != null) {
            holder.onMapReady(holder.mGoogleMap);
        }
    }

    @Override
    public void onViewRecycled(RouteHolder holder) {
        if (holder.mGoogleMap != null) {
            holder.mGoogleMap.clear();
            holder.mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

    public void addRoutes(List<Route> routes) {
        for (Route route : routes) {
            mRoutes.add(route);
            notifyItemInserted(mRoutes.size() - 1);
        }
    }
}
