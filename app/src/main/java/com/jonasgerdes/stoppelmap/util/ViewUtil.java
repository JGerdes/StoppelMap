package com.jonasgerdes.stoppelmap.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Jonas on 07.06.2016.
 */
public class ViewUtil {

    public static int dpToPx(Context context, float dp) {
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return value;
    }

}
