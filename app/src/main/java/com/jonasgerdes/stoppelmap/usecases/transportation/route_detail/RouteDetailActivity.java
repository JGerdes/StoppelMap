package com.jonasgerdes.stoppelmap.usecases.transportation.route_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.transportation.Route;

public class RouteDetailActivity extends AppCompatActivity {

    private static final String EXTRA_ROUTE_ID = "EXTRA_ROUTE_ID";
    private static final String EXTRA_ROUTE_NAME = "EXTRA_ROUTE_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null && getIntent().getExtras() != null) {
            setTitle(getIntent().getStringExtra(EXTRA_ROUTE_NAME));
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public static Intent createIntent(Context conntext, Route route) {
        Intent intent = new Intent(conntext, RouteDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putInt(EXTRA_ROUTE_ID, route.getId());
        extras.putString(EXTRA_ROUTE_NAME, route.getName());
        intent.putExtras(extras);
        return intent;
    }
}
