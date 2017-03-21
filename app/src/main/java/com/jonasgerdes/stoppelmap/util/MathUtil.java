package com.jonasgerdes.stoppelmap.util;

/**
 * Created by jonas on 22.02.2017.
 */

public class MathUtil {

    public static float limit(float lower, float upper, float value) {
        return Math.max(lower, Math.min(upper, value));
    }
}
