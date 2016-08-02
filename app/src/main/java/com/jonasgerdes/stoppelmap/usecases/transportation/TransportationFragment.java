package com.jonasgerdes.stoppelmap.usecases.transportation;

import android.content.Intent;
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
import com.jonasgerdes.stoppelmap.model.transportation.Route;
import com.jonasgerdes.stoppelmap.usecases.transportation.route_detail.RouteDetailActivity;
import com.jonasgerdes.stoppelmap.usecases.transportation.route_list.RouteListAdapter;
import com.jonasgerdes.stoppelmap.usecases.transportation.station_detail.StationDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Jonas on 03.07.2016.
 */
public class TransportationFragment extends Fragment {
    private static final String TAG = "TransportationFragment";

    @BindView(R.id.route_list)
    RecyclerView mRouteList;

    private Unbinder mUnbinder;
    private RouteListAdapter mRoutesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transportation, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);

        MainActivity activity = (MainActivity) getActivity();
        activity.setTitle(getString(R.string.navigation_transportation));
        activity.setCheckedDrawerIcon(R.id.nav_transportation);

        mRoutesAdapter = new RouteListAdapter();
        mRouteList.setAdapter(mRoutesAdapter);
        mRouteList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRoutesAdapter.setRouteSelectedListener(new RouteListAdapter.RouteSelectedListener() {
            @Override
            public void onRouteSelected(Route route) {
                Intent intent = RouteDetailActivity.createIntent(getContext(), route);
                startActivity(intent);
            }

            @Override
            public void onReturnsSelected(Route route) {
                Intent intent =
                        StationDetailActivity.createIntent(getContext(), route.getReturnStation());
                startActivity(intent);
            }
        });


        RealmResults<Route> results = StoppelMapApp.getViaActivity(getActivity())
                .getRealm().where(Route.class).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<Route>>() {
            @Override
            public void onChange(RealmResults<Route> routes) {
                mRoutesAdapter.setRoutes(routes);
            }
        });

    }


    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    public static TransportationFragment newInstance() {

        Bundle args = new Bundle();

        TransportationFragment fragment = new TransportationFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
