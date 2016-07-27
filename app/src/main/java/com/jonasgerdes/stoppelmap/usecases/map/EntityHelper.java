package com.jonasgerdes.stoppelmap.usecases.map;

import android.content.Context;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;

/**
 * Created by Jonas on 27.07.2016.
 */
public class EntityHelper {

    public static String getHeaderFile(Context context, MapEntity mapEntity) {
        String headerFile = mapEntity.getHeaderImageFile();

        if (headerFile == null || headerFile.trim().isEmpty()) {
            String[] placeholders = context.getResources()
                    .getStringArray(R.array.asset_header_placeholders);
            int type = mapEntity.getType();
            if (type < 0 && type >= placeholders.length) {
                type = 0;
            }
            headerFile = placeholders[type];
        }
        return headerFile;
    }
}
