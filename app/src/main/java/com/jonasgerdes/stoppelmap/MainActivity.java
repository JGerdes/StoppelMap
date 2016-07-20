package com.jonasgerdes.stoppelmap;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jonasgerdes.stoppelmap.usecases.about.AboutFragment;
import com.jonasgerdes.stoppelmap.usecases.map.MapFragment;
import com.jonasgerdes.stoppelmap.usecases.transportation.TransportationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;

    private BackPressListener mBackPressListener;

    public interface BackPressListener {
        boolean onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();


        mNavigationView.setNavigationItemSelectedListener(this);
        loadFragment(MapFragment.newInstance(), false);
        mNavigationView.setCheckedItem(R.id.nav_map);

    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
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
//            case R.id.nav_schedule:
//                loadFragment(ScheduleFragment.newInstance());
//                setTitle(item.getTitle());
//                break;
            case R.id.nav_transportation:
                loadFragment(TransportationFragment.newInstance());
                setTitle(item.getTitle());
                break;
            case R.id.nav_about:
                loadFragment(AboutFragment.newInstance());
                setTitle(item.getTitle());
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
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

}
