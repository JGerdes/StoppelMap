package com.jonasgerdes.stoppelmap.usecases.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.model.schedule.Event;
import com.jonasgerdes.stoppelmap.model.transportation.DepartureDay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmResults;

/**
 * Created by Jonas on 04.08.2016.
 */
public class EventDayFragment extends Fragment {

    private static final String ARGUMENT_DAY = "ARGUMENT_DAY";

    private Unbinder mUnbinder;
    @BindView(R.id.depatures)
    RecyclerView mEventList;
    EventAdapter mEventAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_fragment_event_day, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        final int dayId = getArguments().getInt(ARGUMENT_DAY);
        RealmResults<Event> events = StoppelMapApp.getViaActivity(getActivity()).getRealm()
                .where(Event.class).equalTo("day", dayId).findAllAsync();
        mEventAdapter = new EventAdapter(events);
        mEventList.setAdapter(mEventAdapter);
        mEventList.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    public static EventDayFragment newInstance(@DepartureDay.Day int day) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_DAY, day);
        EventDayFragment fragment = new EventDayFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
