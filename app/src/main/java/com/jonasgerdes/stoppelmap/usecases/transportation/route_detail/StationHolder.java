package com.jonasgerdes.stoppelmap.usecases.transportation.route_detail;

import android.view.View;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.transportation.Depature;
import com.jonasgerdes.stoppelmap.model.transportation.Station;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 10.07.2016.
 */
public class StationHolder extends AbstractStationHolder {

    private static SimpleDateFormat FORMAT_NEXT_TIME = new SimpleDateFormat("kk:MM");

    @BindView(R.id.name)
    TextView mName;

    @BindView(R.id.depature_1)
    TextView mDepature1;

    @BindView(R.id.depature_2)
    TextView mDepature2;

    @BindView(R.id.depature_3)
    TextView mDepature3;

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

        mName.setText(station.getName());

        mDepature1.setVisibility(View.GONE);
        mDepature2.setVisibility(View.GONE);
        mDepature3.setVisibility(View.GONE);


        List<Depature> nextDepatures = station.getDays().get(0).getDepatures();
        String timeString;
        for (int i = 0; i < nextDepatures.size() && i < mDepatures.size(); i++) {
            timeString = FORMAT_NEXT_TIME.format(nextDepatures.get(i).getTime());
            mDepatures.get(i).setText(String.format("%s Uhr", timeString));
            mDepatures.get(i).setVisibility(View.VISIBLE);
        }

    }
}
