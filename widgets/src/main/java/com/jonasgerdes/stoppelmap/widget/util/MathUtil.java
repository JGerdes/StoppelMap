package com.jonasgerdes.stoppelmap.widget.util;

public class MathUtil {

    public static float limit(float lowerLimit, float upperLimit, float value) {
        return Math.max(lowerLimit, Math.min(upperLimit, value));
    }
}
