package com.jonasgerdes.stoppelmap.model.map;

import android.support.annotation.DrawableRes;

import com.jonasgerdes.stoppelmap.R;

import java.util.HashMap;

/**
 * Created by Jonas on 19.07.2016.
 */
public class Icon {
    public
    @DrawableRes
    int drawable;
    public String title;

    public Icon(@DrawableRes int drawable, String title) {
        this.drawable = drawable;
        this.title = title;
    }

    public static final HashMap<String, Icon> ICONS;

    static {
        HashMap<String, Icon> icons = new HashMap<>();

        icons.put("first-aid", new Icon(R.drawable.ic_local_hospital_black_24dp, "Erste Hilfe"));
        icons.put("atm", new Icon(R.drawable.ic_local_atm_black_24dp, "Geldautomat"));
        icons.put("info", new Icon(R.drawable.ic_info_outline_black_24dp, "Information"));
        icons.put("bus", new Icon(R.drawable.ic_directions_bus_black_24dp, "Bushaltestelle"));
        icons.put("taxi", new Icon(R.drawable.ic_local_taxi_black_24dp, "Taxistand"));
        icons.put("parking", new Icon(R.drawable.ic_directions_car_black_24dp, "Parkplatz"));
        icons.put("beer", new Icon(R.drawable.ic_beer_black_24dp, "Ausschank"));
        icons.put("food", new Icon(R.drawable.ic_local_dining_black_24dp, "Speisen"));
        icons.put("ice", new Icon(R.drawable.ic_ice_cream_black_24dp, "Eis"));
        icons.put("sweets", new Icon(R.drawable.ic_candy_black_24dp, "Süßwaren"));
        icons.put("wc", new Icon(R.drawable.ic_wc_black_24dp, "Toiletten"));
        icons.put("tent", new Icon(R.drawable.ic_tent_black_24dp, "Festzelt"));
        icons.put("coffee", new Icon(R.drawable.ic_local_cafe_black_24dp, "Café"));

        icons.put("attraction", new Icon(R.drawable.ic_fairground_black_24dp, "Fahrgeschäft"));
        icons.put("kidsride", new Icon(R.drawable.ic_child_care_black_24dp, "Für Kinder"));
        icons.put("games", new Icon(R.drawable.ic_trophy_black_24dp, "Spielstand"));
        icons.put("shooting_games", new Icon(R.drawable.ic_shooting_black_24dp, "Schießbude"));
        icons.put("lottery", new Icon(R.drawable.ic_local_activity_black_24dp, "Verlosung"));

        ICONS = icons;
    }
}
