package com.jonasgerdes.stoppelmap;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.jonasgerdes.stoppelmap.deeplink.DeeplinkHandler;
import com.jonasgerdes.stoppelmap.deeplink.action.Action;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.version.Version;
import com.jonasgerdes.stoppelmap.usecases.about.AboutFragment;
import com.jonasgerdes.stoppelmap.usecases.map.MapFragment;
import com.jonasgerdes.stoppelmap.usecases.schedule.ScheduleFragment;
import com.jonasgerdes.stoppelmap.usecases.transportation.TransportationFragment;
import com.jonasgerdes.stoppelmap.usecases.tutorial.TutorialActivity;
import com.jonasgerdes.stoppelmap.versioning.VersionHelper;
import com.jonasgerdes.stoppelmap.views.SearchCardView;
import com.jonasgerdes.stoppelmap.views.interfaces.TabLayoutProvider;
import com.stephentuso.welcome.WelcomeScreenHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayoutProvider {
    private static final String TAG = "MainActivity";
    private static final long ANIMATION_DURATION = 300;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;
    @BindView(R.id.search_container)
    protected SearchCardView mSearchView;
    @BindView(R.id.tabs)
    protected TabLayout mTabLayout;

    @BindView(R.id.old_data_hint)
    View mOldDataHint;

    private WelcomeScreenHelper mWelcomeScreenHelper;
    private BackPressListener mBackPressListener;
    private AlertDialog mUpdateHint;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;


    public interface BackPressListener {
        /**
         * @return true if event was consumed and shouldn't trigger any other actions anymore
         */
        boolean onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mWelcomeScreenHelper = new WelcomeScreenHelper(this, TutorialActivity.class);
        mWelcomeScreenHelper.show(savedInstanceState);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerToggle.onDrawerOpened(drawerView);
                mSearchView.hide();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerToggle.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                mDrawerToggle.onDrawerStateChanged(newState);
            }
        });
        mDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        mSearchView.setUpWith(this, mToolbar, R.id.options_search);
        checkVersion();


        DeeplinkHandler deeplinkHandler = new DeeplinkHandler();
        Action action = deeplinkHandler.handleIntent(getIntent());
        if (action != null) {
            if (!action.execute(this)) {
                showDefaultFragment();
            }
        } else {
            showDefaultFragment();
        }

    }

    private void showDefaultFragment() {
        loadFragment(MapFragment.newInstance(), false);
        mNavigationView.setCheckedItem(R.id.nav_map);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWelcomeScreenHelper.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        if (mUpdateHint != null) {
            mUpdateHint.dismiss();
        }
        super.onDestroy();
    }

    public void checkVersion() {
        //don't check while debugging
        if (getResources().getBoolean(R.bool.is_debug)) {
            return;
        }
        VersionHelper.requestVersionInfo(this, new VersionHelper.OnVersionAvailableListener() {
            @Override
            public void onVersionAvailable(final Version version) {
                if (version != null) {
                    final int currentVersion = VersionHelper.getVersionCode(MainActivity.this);
                    if (currentVersion != -1 && currentVersion < version.code) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mUpdateHint = new AlertDialog.Builder(MainActivity.this, R.style.StoppelMapAlert)
                                        .setTitle(getString(R.string.dialog_update_title))
                                        .setMessage(getString(R.string.dialog_update_message, version.name))
                                        .setPositiveButton("Aktualisieren", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                final String appPackageName = getPackageName();
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }
                                            }
                                        })
                                        .setNegativeButton("Später", null)
                                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                mUpdateHint = null;
                                            }
                                        })
                                        .show();

                            }
                        });


                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else if (mSearchView.isVisible()) {
            mSearchView.hide();
        } else {
            if (mBackPressListener != null) {
                if (mBackPressListener.onBackPressed()) {
                    return;
                }
            }
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_map:
                loadFragment(MapFragment.newInstance());
                setTitle(getString(R.string.app_name));
                break;
            case R.id.nav_schedule:
                loadFragment(ScheduleFragment.newInstance());
                setTitle(item.getTitle());
                break;
            case R.id.nav_transportation:
                loadFragment(TransportationFragment.newInstance());
                setTitle(item.getTitle());
                break;
            case R.id.nav_about:
                loadFragment(AboutFragment.newInstance());
                setTitle(item.getTitle());
                break;
            case R.id.nav_tutorial:
                mWelcomeScreenHelper.forceShow();
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        loadFragment(fragment, true);
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        if (fragment instanceof BackPressListener) {
            mBackPressListener = (BackPressListener) fragment;
        } else {
            mBackPressListener = null;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public SearchCardView getSearchView() {
        return mSearchView;
    }


    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.syncState();
            mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            invalidateOptionsMenu();

        } else {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mDrawerToggle.syncState();
            mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            invalidateOptionsMenu();
        }
    }

    public void setTitle(String title) {
        mToolbar.setTitle(title);
    }

    public void setCheckedDrawerIcon(@IdRes int id) {
        mNavigationView.setCheckedItem(id);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        StoppelMapApp.getViaActivity(this).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    public void showMapWithEntity(MapEntity mapEntity) {
        showMapWithEntity(mapEntity, true);
    }

    public void showMapWithEntity(MapEntity mapEntity, boolean addToBackstack) {
        Fragment fragment = MapFragment.newInstance(mapEntity);
        loadFragment(fragment, addToBackstack);
    }


    @OnClick(R.id.old_data_hint_close)
    public void hideOldDataHint() {
        mOldDataHint.setVisibility(View.GONE);
    }
}
