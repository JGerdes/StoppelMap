package com.jonasgerdes.stoppelmap.usecases.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.schedule.Event;
import com.jonasgerdes.stoppelmap.util.StringUtil;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 04.08.2016.
 */
public class EventHolder extends RecyclerView.ViewHolder {
    private static SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("kk:mm");

    @BindView(R.id.name)
    TextView mName;

    @BindView(R.id.description)
    TextView mDescription;

    @BindView(R.id.people)
    TextView mPeople;

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
        mName.setText(event.getName());
        setTextOrHide(mDescription, event.getDescription());
        setTextOrHide(mPeople, StringUtil.concat(event.getArtists(), ", "));
        mTime.setText(getTimeString(event));

        if (event.getLocation() != null) {
            mLocations.setVisibility(View.VISIBLE);
            mLocationButton.setVisibility(View.VISIBLE);
            mLocations.setText(event.getLocation().getName());
        } else {
            mLocations.setVisibility(View.GONE);
            mLocationButton.setVisibility(View.GONE);
        }

        if (event.getFacebookUrl() != null) {
            mFacebookButton.setVisibility(View.VISIBLE);
        } else {
            mFacebookButton.setVisibility(View.GONE);
        }

    }

    private String getTimeString(Event event) {
        String time;
        if (event.getEnd() != null) {
            time = String.format(
                    "%s - %s Uhr",
                    FORMAT_TIME.format(event.getStart()),
                    FORMAT_TIME.format(event.getEnd())
            );
        } else {
            time = String.format(
                    "%s Uhr",
                    FORMAT_TIME.format(event.getStart())
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
