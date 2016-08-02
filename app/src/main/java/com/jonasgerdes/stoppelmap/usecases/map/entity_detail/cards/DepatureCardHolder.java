package com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.model.transportation.Departure;
import com.jonasgerdes.stoppelmap.model.transportation.DepartureDay;
import com.jonasgerdes.stoppelmap.model.transportation.Route;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCard;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCardHolder;
import com.jonasgerdes.stoppelmap.util.ViewUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        Calendar now = StoppelMapApp.getCurrentCalendar();
        for (Route route : routes) {
            departure = route.getReturnStation().getNextDepature(now);
            title = new TextView(context);
            title.setText(route.getName());
            title.setPadding(0, 0, padRight, padBottom);
            time = new TextView(context);
            if (departure != null) {
                int today = DepartureDay.getDayFromCalendar(now);
                //never show depature of next day
                if (departure.getDay() != today) {
                    departure = route.getReturnStation().getDays().get(today).getDepartures().last();
                }
                String timeString = FORMAT_NEXT_TIME.format(departure.getTime());
                if (departure.getComment() != null && !departure.getComment().isEmpty()) {
                    time.setText(String.format("%s Uhr (%s)", timeString, departure.getComment()));
                } else {
                    time.setText(String.format("%s Uhr", timeString));
                }
            } else {
                time.setText("keine");
            }
            time.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            time.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            time.setLines(1);
            time.setHorizontallyScrolling(true);
            time.setSelected(true);
            time.setSingleLine(true);
            time.setMarqueeRepeatLimit(-1);

            mDepatures.addView(title);
            mDepatures.addView(time);
        }
    }
}
