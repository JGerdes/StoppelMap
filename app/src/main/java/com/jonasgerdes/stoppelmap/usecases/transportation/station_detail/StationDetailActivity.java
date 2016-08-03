package com.jonasgerdes.stoppelmap.usecases.transportation.station_detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.model.transportation.Price;
import com.jonasgerdes.stoppelmap.model.transportation.Station;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationDetailActivity extends AppCompatActivity {
    private static final String TAG = "StationDetailActivity";

    private static final String EXTRA_STATION_ID = "EXTRA_STATION_ID";
    private static final String EXTRA_STATION_NAME = "EXTRA_STATION_NAME";

    @BindView(R.id.tabs)
    TabLayout mTabs;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private DepartureDayFragmentAdapter mDayPageAdapter;
    private Station mStation;


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


        mStation = StoppelMapApp.getViaActivity(this)
                .getRealm().where(Station.class).equalTo("uuid", stationUuid).findFirst();

        mDayPageAdapter = new DepartureDayFragmentAdapter(this, getSupportFragmentManager(), mStation);
        mViewPager.setAdapter(mDayPageAdapter);
        mTabs.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.price:
                showPriceDialog();
                return true;
            case R.id.directions:
                openDirections();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openDirections() {
        double lat = mStation.getGeoLocation().getLat();
        double lng = mStation.getGeoLocation().getLng();
        String schema = "google.navigation:q=%f,%f&mode=w";
        String uriString = String.format(Locale.US, schema, lat, lng);
        Log.d(TAG, "openDirections: " + uriString);
        Uri gmmIntentUri = Uri.parse(uriString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            schema = "https://www.google.com/maps/preview?saddr=My+Location&daddr=%f,%f&dirflg=w";
            uriString = String.format(Locale.US, schema, lat, lng);
            Log.d(TAG, "openDirections: " + uriString);
            gmmIntentUri = Uri.parse(uriString);
            mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            startActivity(mapIntent);
        }

    }

    private void showPriceDialog() {
        GridLayout priceGrid = (GridLayout) LayoutInflater.from(this)
                .inflate(R.layout.transportation_sation_detail_price, null);

        String[] prices = new String[4];

        for (int i = 0; i < prices.length / 2; i++) {
            if (mStation.getPrices().size() >= i) {
                Price price = mStation.getPrices().get(i);
                prices[i * 2] = price.getType();
                prices[i * 2 + 1] = String.format("%.2f€", price.getPrice() / 100f);
            }
        }

        for (int i = 0; i < prices.length && i < priceGrid.getChildCount(); i++) {
            ((TextView) priceGrid.getChildAt(i)).setText(prices[i]);
        }


        new AlertDialog.Builder(this, R.style.StoppelMapAlert)
                .setTitle("Preise ab " + mStation.getName())
                .setView(priceGrid)
                .setPositiveButton("Ok", null)
                .show();
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
