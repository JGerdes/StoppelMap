package com.jonasgerdes.stoppelmap.usecases.map.entity_detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.transportation.Route;
import com.jonasgerdes.stoppelmap.usecases.map.EntityHelper;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards.DepatureCardHolder;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards.IconsEntityCardHolder;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards.InfoEntityCardHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by Jonas on 17.07.2016.
 */
public class EntityDetailActivity extends AppCompatActivity {
    private static final String TAG = "EntityDetailActivity";
    private static final String ENTITY_UUID = "ENTITY_UUID";

    private static final String UUID_BUS_STOP = "busbahnhof";

    @BindView(R.id.header)
    ImageView mHeaderImage;

    @BindView(R.id.card_list)
    RecyclerView mCardList;

    private MapEntity mEntity;

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

        String entityUuid = getIntent().getExtras().getString(ENTITY_UUID);
        mEntity = StoppelMapApp.getViaActivity(this).getRealm()
                .where(MapEntity.class).equalTo("uuid", entityUuid).findFirst();

        setTitle(mEntity.getName());

        String headerFile = EntityHelper.getHeaderFile(this, mEntity);
        String headerPath = getString(R.string.asset_map_entity_header_dir, headerFile);
        Glide.with(this)
                .load(Uri.parse(headerPath))
                .into(mHeaderImage);


        EntityCardAdapter adapter = new EntityCardAdapter();
        mCardList.setAdapter(adapter);
        mCardList.setLayoutManager(new LinearLayoutManager(this));

        if (mEntity.getIcons() != null && mEntity.getIcons().size() > 0) {
            adapter.addEntityCard(new EntityCard(IconsEntityCardHolder.LAYOUT, mEntity));
        }

        if (mEntity.getInfo() != null && mEntity.getInfo().getText() != null) {
            adapter.addEntityCard(new EntityCard(InfoEntityCardHolder.LAYOUT, mEntity));
        }

        if (UUID_BUS_STOP.equals(mEntity.getUuid())) {
            EntityCard card = new EntityCard(DepatureCardHolder.LAYOUT, mEntity);
            Realm realm = StoppelMapApp.getViaActivity(this).getRealm();
            card.setExtraData(realm.where(Route.class).findAll());
            adapter.addEntityCard(card);
        }
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

    public static Intent createIntent(Context context, MapEntity entity) {
        Intent intent = new Intent(context, EntityDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ENTITY_UUID, entity.getUuid());
        intent.putExtras(extras);
        return intent;
    }
}
