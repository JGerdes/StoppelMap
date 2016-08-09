package com.jonasgerdes.stoppelmap.usecases.transportation.route_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.model.transportation.Departure;
import com.jonasgerdes.stoppelmap.model.transportation.DepartureDay;
import com.jonasgerdes.stoppelmap.model.transportation.Route;
import com.jonasgerdes.stoppelmap.model.transportation.Station;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 09.07.2016.
 */
public class RouteHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

    private static SimpleDateFormat FORMAT_NEXT_TIME = new SimpleDateFormat("kk:mm");
    private static LatLng GEO_POSITION_STOPPELMARKT = new LatLng(52.743618, 8.299542);

    @BindView(R.id.name)
    TextView mName;

    @BindView(R.id.icon)
    ImageView mIcon;

    @BindView(R.id.next_time)
    TextView mNextTime;

    @BindView(R.id.next_station)
    TextView mNextStation;

    @BindView(R.id.map_view)
    MapView mMapView;

    @BindView(R.id.details)
    Button mDetailsButton;

    @BindView(R.id.returns)
    Button mReturnButtons;

    GoogleMap mGoogleMap;

    private List<LatLng> mStationLocations;


    public RouteHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mMapView.onCreate(null);
        mMapView.onResume();
        mMapView.setClickable(false);
        mMapView.getMapAsync(this);

        mStationLocations = new ArrayList<>();
    }

    public void onBind(Route route) {
        Context context = itemView.getContext();
        mName.setText(route.getName());
        Station nextStation = null;
        StoppelMapApp app = StoppelMapApp.getViaContext(context);
        if (app != null) {
            nextStation = route.getNearestStation(app.getLastPosition());
        }
        if (nextStation == null) {
            route.getStations().first();
        }
        Departure departure = nextStation.getNextDepature(StoppelMapApp.getCurrentCalendar());
        if (departure != null) {
            mNextStation.setText(String.format("ab %s", nextStation.getName()));
            mNextStation.setVisibility(View.VISIBLE);
            mNextTime.setVisibility(View.VISIBLE);
            String timeString = FORMAT_NEXT_TIME.format(departure.getTime());
            int departureDay = departure.getDay();
            int todaysDay = DepartureDay.getDayFromCalendar(StoppelMapApp.getCurrentCalendar());
            String depatureString;
            if (departureDay == todaysDay) {
                depatureString = String.format("um %s Uhr ab", timeString);
            } else {
                String dayPrefix =
                        context.getResources().getStringArray(R.array.days)[departureDay];
                depatureString = String.format("%s um %s Uhr", dayPrefix, timeString);
            }
            mNextTime.setText(depatureString);

        } else {
            mNextStation.setVisibility(View.INVISIBLE);
            mNextTime.setText(R.string.transportation_hint_no_more_depatures);
        }
        mStationLocations.clear();
        for (Station station : route.getStations()) {
            mStationLocations.add(station.getGeoLocation().toLatLng());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mGoogleMap == null) {
            MapsInitializer.initialize(itemView.getContext().getApplicationContext());
            mGoogleMap = googleMap;
            mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    //prevent google maps top open
                }
            });
        }

        if (mStationLocations.size() > 0) {
            LatLngBounds.Builder boundBuilder = LatLngBounds.builder();
            //boundBuilder.include(GEO_POSITION_STOPPELMARKT);
            MarkerOptions options = new MarkerOptions();
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_9dp));
            for (LatLng stationLocation : mStationLocations) {
                boundBuilder.include(stationLocation);
                options.position(stationLocation);
                mGoogleMap.addMarker(options);
            }
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundBuilder.build(), 5));
        }

    }
}
