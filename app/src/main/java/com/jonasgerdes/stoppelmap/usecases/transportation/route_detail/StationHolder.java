package com.jonasgerdes.stoppelmap.usecases.transportation.route_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.transportation.Station;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 10.07.2016.
 */
public class StationHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name)
    TextView mName;

    public StationHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(Station station) {
        mName.setText(station.getName());
    }
}
