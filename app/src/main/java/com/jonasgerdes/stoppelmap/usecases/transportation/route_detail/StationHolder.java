package com.jonasgerdes.stoppelmap.usecases.transportation.route_detail;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.model.transportation.Departure;
import com.jonasgerdes.stoppelmap.model.transportation.DepartureDay;
import com.jonasgerdes.stoppelmap.model.transportation.Station;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 10.07.2016.
 */
public class StationHolder extends AbstractStationHolder {

    private static SimpleDateFormat FORMAT_NEXT_TIME = new SimpleDateFormat("kk:mm");

    @BindView(R.id.name)
    TextView mName;

    @BindView(R.id.depature_1)
    TextView mDepature1;

    @BindView(R.id.depature_2)
    TextView mDepature2;

    @BindView(R.id.depature_3)
    TextView mDepature3;

    @BindView(R.id.details)
    Button mDetailsButton;

    List<TextView> mDepatures;


    public StationHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mDepatures = new ArrayList<>();
        mDepatures.add(mDepature1);
        mDepatures.add(mDepature2);
        mDepatures.add(mDepature3);
    }


    public void onBind(Station station, boolean showTopNode, boolean showBottomNode) {

        onBind(showTopNode, showBottomNode);
        Context context = itemView.getContext();

        mName.setText(station.getName());

        mDepature1.setVisibility(View.GONE);
        mDepature2.setVisibility(View.GONE);
        mDepature3.setVisibility(View.GONE);


        Calendar now = StoppelMapApp.getCurrentCalendar();
        int todaysDay = DepartureDay.getDayFromCalendar(now);
        List<Departure> nextDepartures = station.getNextDepatures(now, 3);
        String timeString;
        for (int i = 0; i < nextDepartures.size() && i < mDepatures.size(); i++) {
            Departure departure = nextDepartures.get(i);
            timeString = FORMAT_NEXT_TIME.format(departure.getTime());
            int departureDay = departure.getDay();
            if (departureDay == todaysDay) {
                mDepatures.get(i).setText(String.format("%s Uhr", timeString));
            } else {
                String dayPrefix =
                        context.getResources().getStringArray(R.array.days)[departureDay];
                mDepatures.get(i).setText(String.format("%s, %s Uhr", dayPrefix, timeString));
            }
            mDepatures.get(i).setVisibility(View.VISIBLE);
        }

        if (nextDepartures.size() == 0) {
            mDepature1.setVisibility(View.VISIBLE);
            mDepature1.setText(R.string.transportation_hint_no_more_depatures);
        }

    }
}
