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
        icons.put("beer", new Icon(R.drawable.ic_beer_black_24dp, "Ausschank"));
        icons.put("food", new Icon(R.drawable.ic_local_dining_black_24dp, "Speisen"));
        icons.put("wc", new Icon(R.drawable.ic_wc_black_24dp, "Toiletten"));

        ICONS = icons;
    }
}
