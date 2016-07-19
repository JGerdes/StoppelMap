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
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityDetailActivity;
import com.jonasgerdes.stoppelmap.util.MapUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmResults;

/**
 * Created by Jonas on 03.07.2016.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapFragment";

    private GoogleMap mMap;
    private Unbinder mUnbinder;
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private RealmResults<MapEntity> mMapEntities;
    private MapEntity mCurrentMapEntity;

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

        mMapEntities = StoppelMapApp.getViaActivity(getActivity()).getRealm()
                .where(MapEntity.class).findAllAsync();

    }

    private void showEntityDetail() {

        if (mCurrentMapEntity != null) {
            Intent intent = EntityDetailActivity.createIntent(getContext(), mCurrentMapEntity);

            Pair<View, String> sharedImage = Pair.create((View) mSheetImage, "entity_image");
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), sharedImage);
            startActivity(intent, options.toBundle());
        } else {
            //inconsistent state, hide bottom sheet
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
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

        for (MapEntity mapEntity : mMapEntities) {
            if (mapEntity.getBounds() == null) {
                continue;
            }
            if (MapUtil.isPointInGeoPolygon(latLng, mapEntity.getBounds())) {
                Log.d(TAG, "onMapClicked:" + mapEntity.getUuid());
                mSheetTitle.setText(mapEntity.getName());
                String headerFile = mapEntity.getHeaderImageFile();

                String headerPath = getString(R.string.asset_map_entity_header_dir, headerFile);
                Glide.with(this)
                        .load(Uri.parse(headerPath))
                        .into(mSheetImage);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                mCurrentMapEntity = mapEntity;
                return;
            }

        }
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mCurrentMapEntity = null;
    }


}
