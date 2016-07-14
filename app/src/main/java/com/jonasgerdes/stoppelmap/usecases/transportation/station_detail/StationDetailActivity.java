package com.jonasgerdes.stoppelmap.usecases.transportation.station_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.model.transportation.Station;

import butterknife.ButterKnife;

public class StationDetailActivity extends AppCompatActivity {

    private static final String EXTRA_STATION_ID = "EXTRA_STATION_ID";
    private static final String EXTRA_STATION_NAME = "EXTRA_STATION_NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getIntent() == null || getIntent().getExtras() == null) {
            // TODO: 10.07.2016 handle
        }

        setTitle(getIntent().getStringExtra(EXTRA_STATION_NAME));
        String stationUuid = getIntent().getStringExtra(EXTRA_STATION_ID);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        Station station = StoppelMapApp.getViaActivity(this)
                .getRealm().where(Station.class).equalTo("uuid", stationUuid).findFirst();


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


    public static Intent createIntent(Context context, Station station) {
        Intent intent = new Intent(context, StationDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_STATION_ID, station.getUuid());
        extras.putString(EXTRA_STATION_NAME, station.getName());
        intent.putExtras(extras);
        return intent;
    }
}
