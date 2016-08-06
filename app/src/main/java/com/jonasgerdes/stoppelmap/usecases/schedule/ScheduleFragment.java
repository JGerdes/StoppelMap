package com.jonasgerdes.stoppelmap.usecases.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.MainActivity;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.schedule.Event;
import com.jonasgerdes.stoppelmap.usecases.schedule.search.ScheduleSearchAdapter;
import com.jonasgerdes.stoppelmap.model.schedule.search.ScheduleSearchResult;
import com.jonasgerdes.stoppelmap.views.SearchCardView;
import com.jonasgerdes.stoppelmap.views.interfaces.TabLayoutProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmResults;

/**
 * Created by Jonas on 03.07.2016.
 */
public class ScheduleFragment extends Fragment {

    private static final String TAG = "ScheduleFragment";
    private static final String ARGUMENT_ENTITY_ID = "ARGUMENT_ENTITY_ID";

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private Unbinder mUnbinder;
    private TabLayout mTabLayout;
    private EventDayFragmentAdapter mDayPageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof TabLayoutProvider) {
            mTabLayout = ((TabLayoutProvider) getActivity()).getTabLayout();
            mTabLayout.setVisibility(View.VISIBLE);
        }
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);

        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.setTitle(getString(R.string.navigation_schedule));
            activity.setCheckedDrawerIcon(R.id.nav_schedule);
        }

        mDayPageAdapter = new EventDayFragmentAdapter(getContext(), getChildFragmentManager());
        if (getArguments() != null) {
            String entityId = getArguments().getString(ARGUMENT_ENTITY_ID);
            if (entityId != null) {
                MapEntity entity = StoppelMapApp.getViaActivity(getActivity()).getRealm()
                        .where(MapEntity.class).equalTo("uuid", entityId).findFirst();
                mDayPageAdapter.setEntity(entity);
            }
        }
        mViewPager.setAdapter(mDayPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        setupSearchView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getSearchView() != null) {
            inflater.inflate(R.menu.menu_map_fragment, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_search:
                SearchCardView searchView = getSearchView();
                if (searchView != null) {
                    searchView.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupSearchView() {
        if (getActivity() instanceof MainActivity) {
            final SearchCardView searchCardView = getSearchView();
            if (searchCardView != null) {
                RealmResults<Event> events = StoppelMapApp.getViaActivity(getActivity()).getRealm()
                        .where(Event.class).findAllAsync();
                ScheduleSearchAdapter searchAdapter = new ScheduleSearchAdapter(events);
                searchCardView.setResultAdapter(searchAdapter);
                searchCardView.setPlaceholderText("Suche in Programm");
                searchAdapter.setSelectedListener(new ScheduleSearchAdapter.OnResultSelectedListener() {
                    @Override
                    public void onResultSelected(ScheduleSearchResult result) {
                        searchCardView.hide();
                        // TODO: 06.08.2016 open
                    }
                });
            }
        }
    }

    private SearchCardView getSearchView() {
        if (getActivity() instanceof MainActivity) {
            return ((MainActivity) getActivity()).getSearchView();
        }
        return null;
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
        return newInstance(null);
    }

    public static ScheduleFragment newInstance(String entityId) {

        Bundle args = new Bundle();
        if (entityId != null) {
            args.putString(ARGUMENT_ENTITY_ID, entityId);
        }

        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
