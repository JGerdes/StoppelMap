package com.jonasgerdes.stoppelmap.usecases.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.MainActivity;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.usecases.transportation.station_detail.DepartureDayFragmentAdapter;
import com.jonasgerdes.stoppelmap.views.interfaces.TabLayoutProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Jonas on 03.07.2016.
 */
public class ScheduleFragment extends Fragment {

    private static final String TAG = "ScheduleFragment";

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private Unbinder mUnbinder;
    private TabLayout mTabLayout;
    private EventDayFragmentAdapter mDayPageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        if (getActivity() instanceof TabLayoutProvider) {
            mTabLayout = ((TabLayoutProvider) getActivity()).getTabLayout();
            mTabLayout.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);

        MainActivity activity = (MainActivity) getActivity();
        activity.setTitle(getString(R.string.navigation_schedule));
        activity.setCheckedDrawerIcon(R.id.nav_schedule);

        mDayPageAdapter = new EventDayFragmentAdapter(getContext(), getChildFragmentManager());
        mViewPager.setAdapter(mDayPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        mTabLayout.setVisibility(View.GONE);
        mTabLayout.setupWithViewPager(null);
        mTabLayout = null;
        super.onDestroyView();
    }


    public static ScheduleFragment newInstance() {

        Bundle args = new Bundle();

        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
