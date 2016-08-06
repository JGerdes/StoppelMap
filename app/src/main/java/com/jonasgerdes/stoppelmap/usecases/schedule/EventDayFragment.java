package com.jonasgerdes.stoppelmap.usecases.schedule;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.MainActivity;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.schedule.Event;
import com.jonasgerdes.stoppelmap.model.transportation.DepartureDay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Jonas on 04.08.2016.
 */
public class EventDayFragment extends Fragment implements EventAdapter.EventActionListener {

    private static final String ARGUMENT_DAY = "ARGUMENT_DAY";
    private static final String ARGUMENT_ENTITY_UUID = "ARGUMENT_ENTITY_UUID";
    private static final String ARGUMENT_ENTITY_TYPE = "ARGUMENT_ENTITY_TYPE";
    private static final String ARGUMENT_START_EVENT = "ARGUMENT_START_EVENT";

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

        RealmQuery<Event> query = StoppelMapApp.getViaActivity(getActivity()).getRealm()
                .where(Event.class).equalTo("day", dayId);
        if (getArguments().getString(ARGUMENT_ENTITY_UUID) != null) {
            query.equalTo("locationUuid", getArguments().getString(ARGUMENT_ENTITY_UUID));
        }
        if (getArguments().getInt(ARGUMENT_ENTITY_TYPE, -1) == MapEntity.TYPE_RIDE) {
            query.or()
                    .equalTo("day", dayId)
                    .equalTo("type", Event.TYPE_GLOBAL_RIDE);
        }
        RealmResults<Event> events = query.findAllSortedAsync("start");
        mEventAdapter = new EventAdapter(events);
        mEventList.setAdapter(mEventAdapter);
        mEventList.setLayoutManager(new LinearLayoutManager(getContext()));

        events.addChangeListener(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(RealmResults<Event> updatedEvents) {
                int startIndex = getIndexOfEvent(getArguments().getString(ARGUMENT_START_EVENT), updatedEvents);
                if (startIndex != -1) {
                    mEventList.scrollToPosition(startIndex);
                }
            }
        });


        mEventAdapter.setActionListener(this);
        if (getArguments().getString(ARGUMENT_ENTITY_UUID) != null) {
            mEventAdapter.setHideLocationButton(true);
        }

    }

    private int getIndexOfEvent(String uuid, List<Event> events) {
        int startIndex = -1;
        if (uuid != null) {
            for (int i = 0; i < events.size(); i++) {
                if (events.get(i).getUuid().equals(uuid)) {
                    startIndex = i;
                    break;
                }
            }
        }
        return startIndex;
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    public static EventDayFragment newInstance(@DepartureDay.Day int day, MapEntity entity) {
        return newInstance(day, entity, null);
    }

    public static EventDayFragment newInstance(@DepartureDay.Day int day, MapEntity entity,
                                               String startEvent) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_DAY, day);
        if (entity != null) {
            args.putString(ARGUMENT_ENTITY_UUID, entity.getUuid());
            args.putInt(ARGUMENT_ENTITY_TYPE, entity.getType());
        }
        if (startEvent != null) {
            args.putString(ARGUMENT_START_EVENT, startEvent);
        }
        EventDayFragment fragment = new EventDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onFacebookAction(String url) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(url));
        startActivity(facebookIntent);
    }

    @Override
    public void onLocationAction(MapEntity location) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showMapWithEntity(location);
        }
    }
}
