package com.jonasgerdes.stoppelmap.usecases.transportation.route_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.model.transportation.Route;
import com.jonasgerdes.stoppelmap.model.transportation.Station;
import com.jonasgerdes.stoppelmap.usecases.transportation.station_detail.StationDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteDetailActivity extends AppCompatActivity {

    private static final String EXTRA_ROUTE_ID = "EXTRA_ROUTE_ID";
    private static final String EXTRA_ROUTE_NAME = "EXTRA_ROUTE_NAME";

    @BindView(R.id.station_list)
    RecyclerView mStationList;

    private StationListAdapter mStationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getIntent() == null || getIntent().getExtras() == null) {
            // TODO: 10.07.2016 handle
        }

        setTitle(getIntent().getStringExtra(EXTRA_ROUTE_NAME));
        String routeUuid = getIntent().getStringExtra(EXTRA_ROUTE_ID);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mStationList.setLayoutManager(new LinearLayoutManager(this));
        mStationAdapter = new StationListAdapter();
        mStationList.setAdapter(mStationAdapter);
        mStationAdapter.setStationSelectedListener(new StationListAdapter.StationSelectedListener() {
            @Override
            public void onStationSelected(Station station) {
                Intent intent = StationDetailActivity.createIntent(RouteDetailActivity.this, station);
                startActivity(intent);
            }
        });

        Route route = StoppelMapApp.getViaActivity(this)
                .getRealm().where(Route.class).equalTo("uuid", routeUuid).findFirst();

        mStationAdapter.setStations(route.getStations());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static Intent createIntent(Context conntext, Route route) {
        Intent intent = new Intent(conntext, RouteDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_ROUTE_ID, route.getUuid());
        extras.putString(EXTRA_ROUTE_NAME, route.getName());
        intent.putExtras(extras);
        return intent;
    }
}
