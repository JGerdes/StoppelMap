package com.jonasgerdes.stoppelmap.widget.heart;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jonas on 23.02.2017.
 */

public class OptionsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mPages;

    public OptionsPagerAdapter(FragmentManager fm, Fragment... pages) {
        super(fm);
        mPages = Arrays.asList(pages);
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
