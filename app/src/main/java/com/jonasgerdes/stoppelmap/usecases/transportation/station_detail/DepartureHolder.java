package com.jonasgerdes.stoppelmap.usecases.transportation.station_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.transportation.Departure;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 15.07.2016.
 */
public class DepartureHolder extends RecyclerView.ViewHolder {

    private static SimpleDateFormat FORMAT_NEXT_TIME = new SimpleDateFormat("kk:mm");

    @BindView(R.id.time)
    TextView mTimeView;

    public DepartureHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(Departure departure, boolean isNext) {
        String time = FORMAT_NEXT_TIME.format(departure.getTime());
        mTimeView.setText(String.format("%s Uhr", time));
    }
}
