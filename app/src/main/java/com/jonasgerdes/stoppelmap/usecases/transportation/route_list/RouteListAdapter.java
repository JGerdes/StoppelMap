package com.jonasgerdes.stoppelmap.usecases.transportation.route_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.transportation.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 09.07.2016.
 */
public class RouteListAdapter extends RecyclerView.Adapter<RouteHolder> {

    public interface RouteSelectedListener {
        void onRouteSelected(Route route);
    }

    private List<Route> mRoutes;
    private int mLastPositionShown;
    private RouteSelectedListener mRouteSelectedListener;

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
        final Route route = mRoutes.get(position);
        holder.onBind(route);
        if (holder.mGoogleMap != null) {
            holder.onMapReady(holder.mGoogleMap);
        }
        setAnimation(holder.itemView, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRouteSelectedListener != null) {
                    mRouteSelectedListener.onRouteSelected(route);
                }
            }
        });

        holder.mDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRouteSelectedListener != null) {
                    mRouteSelectedListener.onRouteSelected(route);
                }
            }
        });
    }

    @Override
    public void onViewRecycled(RouteHolder holder) {
        if (holder.mGoogleMap != null) {
            holder.mGoogleMap.clear();
        }
    }

    @Override
    public void onViewDetachedFromWindow(RouteHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > mLastPositionShown) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(),
                    R.anim.card_slide_in);
            viewToAnimate.startAnimation(animation);
            mLastPositionShown = position;
        }
    }

    public void setRoutes(List<Route> routes) {
        mRoutes.clear();
        mRoutes.addAll(routes);
        notifyItemRangeInserted(0, mRoutes.size());
    }

    public void setRouteSelectedListener(RouteSelectedListener routeSelectedListener) {
        mRouteSelectedListener = routeSelectedListener;
    }
}
