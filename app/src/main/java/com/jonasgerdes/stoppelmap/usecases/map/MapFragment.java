package com.jonasgerdes.stoppelmap.usecases.map;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.jonasgerdes.stoppelmap.MainActivity;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.model.map.Icon;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityDetailActivity;
import com.jonasgerdes.stoppelmap.usecases.map.search.MapEntitySearchAdapter;
import com.jonasgerdes.stoppelmap.util.MapUtil;
import com.jonasgerdes.stoppelmap.views.SearchCardView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

/**
 * Created by Jonas on 03.07.2016.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, MainActivity.BackPressListener {
    private static final String TAG = "MapFragment";

    private GoogleMap mMap;
    private Unbinder mUnbinder;
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private List<MapEntity> mMapEntities;
    private MapEntity mCurrentMapEntity;
    private MarkerManager mMarkerManager;

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

    @BindView(R.id.bottom_sheet_icons)
    ViewGroup mBottomSheetIcons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mUnbinder = ButterKnife.bind(this, view);
        if (savedInstanceState == null) {
            FragmentManager fm = getChildFragmentManager();
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.map_placeholder, mapFragment).commitAllowingStateLoss();
            mapFragment.getMapAsync(MapFragment.this);
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

        final Realm realm = StoppelMapApp.getViaActivity(getActivity()).getRealm();

        Log.d(TAG, "copy entities from realm");
        long t = System.currentTimeMillis();
        mMapEntities = realm.copyFromRealm(realm.where(MapEntity.class).findAll());
        t = System.currentTimeMillis() - t;
        Log.d(TAG, "copy from realm took " + t + "ms");

        if (getActivity() instanceof MainActivity) {
            MapEntitySearchAdapter searchAdapter = new MapEntitySearchAdapter(mMapEntities);
            final SearchCardView searchCardView = ((MainActivity) getActivity()).getSearchView();
            searchCardView.setResultAdapter(searchAdapter);
            searchAdapter.setSelectedListener(new MapEntitySearchAdapter.OnResultSelectedListener() {
                @Override
                public void onResultSelected(final SearchResult result) {
                    searchCardView.hide();
                    mBottomSheet.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showBottomBarWith(result.mapEntity);
                        }
                    }, 100);

                }
            });
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_search:
                hideBottomSheet();
                getSearchView().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private SearchCardView getSearchView() {
        return ((MainActivity) getActivity()).getSearchView();
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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                MapEntity entity = mMarkerManager.getEntityForMarker(marker);
                if (entity != null) {
                    showBottomBarWith(entity);
                }
                return false;
            }
        });

        mMarkerManager = new MarkerManager(getContext(), mMap);
        mMarkerManager.generateMarkers(mMapEntities);

    }

    private void onMapClicked(LatLng latLng) {

        for (MapEntity mapEntity : mMapEntities) {
            if (mapEntity.getBounds() == null) {
                continue;
            }
            if (MapUtil.isPointInGeoPolygon(latLng, mapEntity.getBounds())) {
                Log.d(TAG, "onMapClicked:" + mapEntity.getUuid());
                showBottomBarWith(mapEntity);
                if (mapEntity.getOrigin() != null) {
                    CameraUpdate update =
                            CameraUpdateFactory.newLatLng(mapEntity.getOrigin().toLatLng());
                    mMap.animateCamera(update);
                }

                return;
            }

        }
        hideBottomSheet();
    }

    private void hideBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mCurrentMapEntity = null;
    }

    private void showBottomBarWith(MapEntity mapEntity) {
        mCurrentMapEntity = mapEntity;
        mSheetTitle.setText(mapEntity.getName());
        String headerFile = mapEntity.getHeaderImageFile();

        String headerPath = getString(R.string.asset_map_entity_header_dir, headerFile);
        Glide.with(this)
                .load(Uri.parse(headerPath))
                .into(mSheetImage);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        for (int i = 0; i < mBottomSheetIcons.getChildCount(); i++) {
            ImageView iconView = (ImageView) mBottomSheetIcons.getChildAt(i);
            if (mapEntity.getIcons() != null
                    && mapEntity.getIcons().size() - 1 >= i) {
                Icon icon = Icon.ICONS.get(mapEntity.getIcons().get(i).getVal());
                if (icon != null) {
                    iconView.setImageResource(icon.drawable);
                    continue;
                }
            }
            iconView.setImageDrawable(null);
        }
    }


    @Override
    public boolean onBackPressed() {
        if (mCurrentMapEntity != null) {
            hideBottomSheet();
            return true;
        }
        return false;
    }

    public static MapFragment newInstance() {

        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
