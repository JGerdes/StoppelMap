package com.jonasgerdes.stoppelmap.usecases.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.schedule.Event;
import com.jonasgerdes.stoppelmap.views.interfaces.TabLayoutProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntityScheduleActivity extends AppCompatActivity implements TabLayoutProvider {
    private static final String TAG = "EntityScheduleActivity";

    private static final String EXTRA_ENTITY_ID = "EXTRA_ENTITY_ID";
    private static final String EXTRA_ENTITY_NAME = "EXTRA_ENTITY_NAME";
    private static final String EXTRA_START_EVENT = "EXTRA_START_EVENT";
    private static final String EXTRA_START_DAY = "EXTRA_START_DAY";

    @BindView(R.id.tabs)
    TabLayout mTabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_schedule);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String entityUuid = getIntent().getStringExtra(EXTRA_ENTITY_ID);
        String entityName = getIntent().getStringExtra(EXTRA_ENTITY_NAME);

        if (entityName != null) {
            setTitle("Programm: " + entityName);
        } else {
            setTitle("Programm");
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        int day = getIntent().getIntExtra(EXTRA_START_DAY, -1);
        String event = getIntent().getStringExtra(EXTRA_START_EVENT);
        Fragment fragment = ScheduleFragment.newInstance(entityUuid, day, event);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commitAllowingStateLoss();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static Intent createIntent(Context context, MapEntity entity) {
        Intent intent = new Intent(context, EntityScheduleActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_ENTITY_ID, entity.getUuid());
        extras.putString(EXTRA_ENTITY_NAME, entity.getName());
        intent.putExtras(extras);
        return intent;
    }

    public static Intent createIntent(Context context, Event event) {
        MapEntity entity = event.getLocation();
        Intent intent;
        Bundle extras;
        if (entity == null) {
            intent = new Intent(context, EntityScheduleActivity.class);
            extras = new Bundle();
        } else {
            intent = createIntent(context, entity);
            extras = intent.getExtras();
        }
        extras.putInt(EXTRA_START_DAY, event.getDay());
        extras.putString(EXTRA_START_EVENT, event.getUuid());

        intent.putExtras(extras);
        return intent;
    }

    @Override
    public TabLayout getTabLayout() {
        return mTabs;
    }
}
