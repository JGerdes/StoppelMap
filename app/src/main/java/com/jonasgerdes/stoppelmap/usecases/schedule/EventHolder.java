package com.jonasgerdes.stoppelmap.usecases.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.schedule.Event;
import com.jonasgerdes.stoppelmap.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 04.08.2016.
 */
public class EventHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "EventHolder";
    private static SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("kk:mm");
    private static SimpleDateFormat FORMAT_TIME_WITH_DAY = new SimpleDateFormat("EE, kk:mm", Locale.GERMAN);

    @BindView(R.id.name)
    TextView mName;

    @BindView(R.id.description)
    TextView mDescription;

    @BindView(R.id.people)
    TextView mPeople;


    @BindView(R.id.music)
    TextView mMusic;

    @BindView(R.id.time)
    TextView mTime;

    @BindView(R.id.location)
    TextView mLocations;

    @BindView(R.id.facebook)
    Button mFacebookButton;

    @BindView(R.id.open_location)
    Button mLocationButton;

    public EventHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(Event event) {
        bindContent(event);
        bindLocation(event.getLocation());
        bindFacebookLink(event.getFacebookUrl());

    }

    public void bindContent(Event event) {
        bindContent(event, false);
    }

    public void bindContent(Event event, boolean showDateOnStart) {
        mName.setText(event.getName());
        setTextOrHide(mDescription, event.getDescription());
        setTextOrHide(mPeople, StringUtil.concat(event.getArtists(), ", "));
        setTextOrHide(mMusic, StringUtil.concatTags(event.getTags(), ", "));
        mTime.setText(getTimeString(event, showDateOnStart ? FORMAT_TIME_WITH_DAY : FORMAT_TIME));
    }

    public void bindFacebookLink(String facebookUrl) {
        if (facebookUrl != null && !facebookUrl.trim().isEmpty()) {
            mFacebookButton.setVisibility(View.VISIBLE);
        } else {
            mFacebookButton.setVisibility(View.GONE);
        }
    }

    public void bindLocation(MapEntity location) {
        if (location != null) {
            mLocations.setVisibility(View.VISIBLE);
            mLocationButton.setVisibility(View.VISIBLE);
            mLocations.setText(location.getName());
        } else {
            mLocations.setVisibility(View.GONE);
            mLocationButton.setVisibility(View.GONE);
        }
    }

    private String getTimeString(Event event, SimpleDateFormat startFormat) {
        String time;
        if (event.getEnd() != null) {
            time = String.format(
                    "%s - %s Uhr",
                    startFormat.format(event.getStart()),
                    FORMAT_TIME.format(event.getEnd())
            );
        } else {
            time = String.format(
                    "%s Uhr",
                    startFormat.format(event.getStart())
            );
        }
        return time;
    }

    private void setTextOrHide(TextView view, String text) {
        if (text == null || text.trim().length() == 0) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        }
    }
}
