package com.jonasgerdes.stoppelmap.usecases.transportation.station_detail;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.transportation.Station;

/**
 * Created by Jonas on 15.07.2016.
 */
public class DepartureDayFragmentAdapter extends FragmentPagerAdapter {

    private final String[] mPageTitles;
    private final Station mStation;

    public DepartureDayFragmentAdapter(Context context, FragmentManager fm, Station station) {
        super(fm);
        mPageTitles = context.getResources().getStringArray(R.array.days);
        mStation = station;
    }

    @Override
    public Fragment getItem(int position) {
        return DepartureDayFragment.newInstance(mStation, position);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPageTitles[position];
    }
}
