package com.jonasgerdes.stoppelmap.usecases.transportation.route_list;

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
import com.jonasgerdes.stoppelmap.model.transportation.Departure;
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

    GoogleMap mGoogleMap;

    private List<LatLng> mStationLocations;


    public RouteHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mMapView.onCreate(null);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        mStationLocations = new ArrayList<>();
    }

    public void onBind(Route route) {
        mName.setText(route.getName());
        Station nextStation = route.getStations().first();
        mNextStation.setText(nextStation.getName());
        Departure departure = nextStation.getDays().first().getDepartures().first();
        String timeString = FORMAT_NEXT_TIME.format(departure.getTime());
        String depatureString = String.format("um %s Uhr ab", timeString);
        mNextTime.setText(depatureString);

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
        }

        if (mStationLocations.size() > 0) {
            LatLngBounds.Builder boundBuilder = LatLngBounds.builder();
            boundBuilder.include(GEO_POSITION_STOPPELMARKT);
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
