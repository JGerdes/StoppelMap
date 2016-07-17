package com.jonasgerdes.stoppelmap.usecases.map.entity_detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jonasgerdes.stoppelmap.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 17.07.2016.
 */
public class EntityDetailActivity extends AppCompatActivity {
    private static final String TAG = "EntityDetailActivity";

    @BindView(R.id.header)
    ImageView mHeaderImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Glide.with(this)
                .load(Uri.parse("file:///android_asset/headers/amtmannsbult.jpg"))
                .into(mHeaderImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, EntityDetailActivity.class);
        Bundle extras = new Bundle();
        intent.putExtras(extras);
        return intent;
    }
}
