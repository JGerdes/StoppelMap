package com.jonasgerdes.stoppelmap.usecases.schedule;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jonasgerdes.stoppelmap.R;

/**
 * Created by Jonas on 04.08.2016.
 */
public class EventDayFragmentAdapter extends FragmentPagerAdapter {

    private final String[] mPageTitles;

    public EventDayFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mPageTitles = context.getResources().getStringArray(R.array.days);
    }

    @Override
    public Fragment getItem(int position) {
        return EventDayFragment.newInstance(position);
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
