package com.jonasgerdes.stoppelmap.usecases.schedule.search;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.schedule.search.ScheduleSearchResult;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 06.08.2016.
 */
public class ScheduleSearchResultHolder extends RecyclerView.ViewHolder {

    private static SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("kk:mm", Locale.GERMAN);
    private static SimpleDateFormat FORMAT_DAY = new SimpleDateFormat("EE", Locale.GERMAN);

    @BindView(R.id.reason)
    TextView mReason;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.location)
    TextView mLocation;

    @BindView(R.id.day)
    TextView mDay;

    @BindView(R.id.time)
    TextView mTime;

    public ScheduleSearchResultHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(ScheduleSearchResult result) {
        Context context = itemView.getContext();
        mTitle.setText(result.getEvent().getName());

        if (result.getReason() == ScheduleSearchResult.REASON_NAME) {
            mReason.setVisibility(View.GONE);
        } else {
            mReason.setVisibility(View.VISIBLE);
            mReason.setText(result.getReasonString());
            int drawable = -1;
            switch (result.getReason()) {
                case ScheduleSearchResult.REASON_ARTIST:
                    drawable = R.drawable.ic_group_black54_16dp;
                    break;
                case ScheduleSearchResult.REASON_TAG:
                    if (result.getReasonString().toLowerCase().contains("kinder")) {
                        drawable = R.drawable.ic_child_care_black54_16dp;
                    } else {
                        drawable = R.drawable.ic_audiotrack_black54_16dp;
                    }
                    break;
            }
            mReason.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(context, drawable), null, null, null
            );
        }

        if (result.getEvent().getLocation() == null) {
            mLocation.setVisibility(View.GONE);
        } else {
            mLocation.setVisibility(View.VISIBLE);
            mLocation.setText(result.getEvent().getLocation().getName());
        }

        mDay.setText(FORMAT_DAY.format(result.getEvent().getStart()));
        mTime.setText(FORMAT_TIME.format(result.getEvent().getStart()));

    }

}
