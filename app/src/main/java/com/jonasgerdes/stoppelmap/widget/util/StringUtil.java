package com.jonasgerdes.stoppelmap.widget.util;

import android.support.annotation.ColorInt;

/**
 * Created by Jonas on 01.08.2016.
 */
public class StringUtil {

    public static String getHexFromColor(@ColorInt int color) {
        return String.format("%06X", (0xFFFFFF & color));
    }
}
