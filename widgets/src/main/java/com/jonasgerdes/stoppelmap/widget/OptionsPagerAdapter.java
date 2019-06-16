package com.jonasgerdes.stoppelmap.widget;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
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
