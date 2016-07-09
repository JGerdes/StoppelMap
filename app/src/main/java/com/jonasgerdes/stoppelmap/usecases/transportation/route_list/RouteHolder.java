package com.jonasgerdes.stoppelmap.usecases.transportation.route_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.transportation.Route;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 09.07.2016.
 */
public class RouteHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.route_name)
    TextView mRouteNameView;

    public RouteHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(Route route) {
        mRouteNameView.setText(route.getName());
    }
}
