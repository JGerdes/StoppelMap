package com.jonasgerdes.stoppelmap;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.jonasgerdes.stoppelmap.data.SearchResult;


public class MainActivity extends ActionBarActivity {


    private static Context context;
    private MapController mapController;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private enum State{DEFAULT, SEARCH};
    private State state = State.DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mapController = new MapController(this);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapController);

        final CoordinatorLayout root = (CoordinatorLayout) findViewById(R.id.rootLayout);

        FloatingActionButton fabBtn = (FloatingActionButton) findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mapController.targetCurrentLocation()){
                    String message = getString(R.string.message_not_in_area);
                    Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
                }

            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open,
                R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if(state == State.SEARCH){
            state = State.DEFAULT;
            invalidateOptionsMenu();
            mapController.present(null);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        if(state == state.DEFAULT) {
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));

            final SearchController search = new SearchController(mapController.getData(), this);

            searchView.setSuggestionsAdapter(search.getAdpater());
            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionClick(int position) {
                    SearchResult result = search.getSelectedById(position);
                    state = State.SEARCH;
                    mapController.present(result);
                    invalidateOptionsMenu();
                    getSupportActionBar().setTitle(result.getTitle());
                    return true;
                }

                @Override
                public boolean onSuggestionSelect(int position) {
                    return true;
                }
            });
            searchView.setOnQueryTextListener(search);
            menu.findItem(R.id.search).setVisible(true);
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        }else{
            menu.findItem(R.id.search).setVisible(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        return true;
    }


    private void setState(State state){
        this.state = state;
        if(state == State.SEARCH){
            drawerLayout.setDrawerListener(null);
        }else{
            drawerLayout.setDrawerListener(drawerToggle);
            mapController.present(null);
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                if(state == State.SEARCH){
                   setState(State.DEFAULT);
                }
        }
        return true;
    }

    public static Context getContext(){
        return context;
    }

    public static int convertDpToPixel(int dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

}
