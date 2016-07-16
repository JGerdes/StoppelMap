package com.jonasgerdes.stoppelmap.usecases.transportation.station_detail;

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
import com.jonasgerdes.stoppelmap.model.transportation.DepartureDay;
import com.jonasgerdes.stoppelmap.model.transportation.Station;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Jonas on 15.07.2016.
 */
public class DepartureDayFragment extends Fragment {

    private static final String ARGUMENT_STATION_UUID = "ARGUMENT_STATION_UUID";
    private static final String ARGUMENT_DAY = "ARGUMENT_DAY";

    private Unbinder mUnbinder;
    private DepatureAdapter mDepatureAdapter;
    @BindView(R.id.depatures)
    RecyclerView mDepatureList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transportation_fragment_depature_day, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        String stationUuid = getArguments().getString(ARGUMENT_STATION_UUID);
        final int dayId = getArguments().getInt(ARGUMENT_DAY);

        mDepatureAdapter = new DepatureAdapter();
        mDepatureList.setAdapter(mDepatureAdapter);
        mDepatureList.setLayoutManager(new LinearLayoutManager(getContext()));

        Station station = StoppelMapApp.getViaActivity(getActivity()).getRealm()
                .where(Station.class).equalTo("uuid", stationUuid).findFirst();
        DepartureDay day = station.getDays().get(dayId);

        mDepatureAdapter.setDepartures(day.getDepartures());
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    public static DepartureDayFragment newInstance(Station station, @DepartureDay.Day int day) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_STATION_UUID, station.getUuid());
        args.putInt(ARGUMENT_DAY, day);
        DepartureDayFragment fragment = new DepartureDayFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
