package com.jonasgerdes.stoppelmap.usecases.map;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityDetailActivity;
import com.jonasgerdes.stoppelmap.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Jonas on 03.07.2016.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapFragment";

    private GoogleMap mMap;
    private Unbinder mUnbinder;
    private BottomSheetBehavior<View> mBottomSheetBehavior;

    @BindView(R.id.bottom_sheet_image)
    ImageView mSheetImage;

    @BindView(R.id.bottom_sheet_title)
    TextView mSheetTitle;

    @BindView(R.id.details)
    Button mSheetDetailButton;

    @BindView(R.id.bottom_sheet)
    View mBottomSheet;

    @BindView(R.id.bottom_sheet_content)
    View mBottomSheetContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        if (savedInstanceState == null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    FragmentManager fm = getChildFragmentManager();
                    SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                    fm.beginTransaction()
                            .replace(R.id.map_placeholder, mapFragment).commitAllowingStateLoss();
                    mapFragment.getMapAsync(MapFragment.this);
                }
            }, 400);
        }

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);


        mSheetDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEntityDetail();
            }
        });

        mBottomSheetContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEntityDetail();
            }
        });

    }

    private void showEntityDetail() {

        Intent intent = EntityDetailActivity.createIntent(getContext());

        Pair<View, String> sharedImage = Pair.create((View) mSheetImage, "entity_image");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), sharedImage);
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    public static MapFragment newInstance() {

        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: 03.07.2016
        } else {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        AssetManager assets = getContext().getAssets();
        mMap.addTileOverlay(
                new TileOverlayOptions().tileProvider(new CustomMapTileProvider(assets))
        );


        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.747995, 8.295607), 16));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                onMapClicked(latLng);
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                onMapClicked(latLng);
            }
        });

    }

    private void onMapClicked(LatLng latLng) {
        final List<LatLng> amtmansbult = new ArrayList<>();
        amtmansbult.add(new LatLng(52.748714570890684, 8.295282572507858));
        amtmansbult.add(new LatLng(52.74875110126543, 8.295550793409348));
        amtmansbult.add(new LatLng(52.74858793202118, 8.2955963909626));
        amtmansbult.add(new LatLng(52.74856520192871, 8.295294642448425));

        if (MapUtil.isPointInPolygon(latLng, amtmansbult)) {
            Log.d(TAG, "onMapClicked: amtmanssbult");
            mSheetTitle.setText("Amtmannsbult");
            Glide.with(this)
                    .load(Uri.parse("file:///android_asset/headers/amtmannsbult.jpg"))
                    .into(mSheetImage);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


}
