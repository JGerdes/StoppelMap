package com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.transportation.Departure;
import com.jonasgerdes.stoppelmap.model.transportation.Route;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCard;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCardHolder;
import com.jonasgerdes.stoppelmap.util.ViewUtil;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 19.07.2016.
 */
public class DepatureCardHolder extends EntityCardHolder {

    public static final int LAYOUT = R.layout.map_entity_card_depature;
    private static SimpleDateFormat FORMAT_NEXT_TIME = new SimpleDateFormat("kk:mm");

    @BindView(R.id.depatures)
    GridLayout mDepatures;

    public DepatureCardHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(EntityCard entityCard) {
        List<Route> routes = entityCard.getExtraData();

        Context context = itemView.getContext();
        TextView title;
        TextView time;
        Departure departure;
        int padBottom = ViewUtil.dpToPx(context, 4);
        int padRight = ViewUtil.dpToPx(context, 32);
        for (Route route : routes) {
            //Todo: select corret one according to time
            departure = route.getStations().first().getDays().first().getDepartures().first();
            title = new TextView(context);
            title.setText(route.getName());
            title.setPadding(0, 0, padRight, padBottom);
            time = new TextView(context);
            String timeString = FORMAT_NEXT_TIME.format(departure.getTime());
            time.setText(String.format("%s Uhr", timeString));

            mDepatures.addView(title);
            mDepatures.addView(time);
        }
    }
}
