package com.jonasgerdes.stoppelmap.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jonasgerdes.stoppelmap.widget.options.OptionPage;

import java.util.List;

/**
 * Created by jonas on 23.02.2017.
 */

public class OptionsPagerAdapter extends FragmentPagerAdapter {

    private List<OptionPage> mPages;

    public OptionsPagerAdapter(FragmentManager fm, List<OptionPage> pages) {
        super(fm);
        mPages = pages;
    }

    @Override
    public Fragment getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public int getCount() {
        return mPages.size();
    }
}
