package com.jonasgerdes.stoppelmap.usecases.schedule;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;

/**
 * Created by Jonas on 04.08.2016.
 */
public class EventDayFragmentAdapter extends FragmentPagerAdapter {

    private final String[] mPageTitles;
    private MapEntity mEntity = null;
    private String mStartEventUuid = null;

    public EventDayFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mPageTitles = context.getResources().getStringArray(R.array.days);
    }

    @Override
    public Fragment getItem(int position) {
        return EventDayFragment.newInstance(position, mEntity, mStartEventUuid);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPageTitles[position];
    }

    public void setEntity(MapEntity entity) {
        mEntity = entity;
    }

    public void setStartEventUuid(String startEventUuid) {
        mStartEventUuid = startEventUuid;
    }
}
