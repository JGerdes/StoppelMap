package com.jonasgerdes.stoppelmap.deeplink.action;

import com.jonasgerdes.stoppelmap.MainActivity;
import com.jonasgerdes.stoppelmap.StoppelMapApp;
import com.jonasgerdes.stoppelmap.deeplink.DeeplinkHandler;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;

import io.realm.Realm;

/**
 * Created by Jonas on 10.08.2016.
 */
public class MapAction extends Action {

    public static final String IDENTIFIER = "map";
    private String mEntityUuid;

    public MapAction(String[] parts) {

        mEntityUuid = parts[2];
    }

    @Override
    public boolean execute(MainActivity mainActivity) {
        Realm realm = StoppelMapApp.getViaActivity(mainActivity).getRealm();
        MapEntity mapEntity = realm.where(MapEntity.class).equalTo("uuid", mEntityUuid).findFirst();
        if (mapEntity == null) {
            return false;
        } else {
            mainActivity.showMapWithEntity(mapEntity, false);
            return true;
        }
    }

    public static String createShareUrl(MapEntity mapEntity) {
        return DeeplinkHandler.createShareUrl(IDENTIFIER, mapEntity.getUuid());
    }
}
