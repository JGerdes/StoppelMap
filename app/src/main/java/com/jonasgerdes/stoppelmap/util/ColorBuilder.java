package com.jonasgerdes.stoppelmap.util;

import android.support.v4.graphics.ColorUtils;

/**
 * Created by jonas on 19.02.2017.
 */

public class ColorBuilder {

    private float mHue;
    private float mSaturation;
    private float mLightness;

    private ColorBuilder(int color) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        mHue = hsl[0];
        mSaturation = hsl[1];
        mLightness = hsl[2];
    }

    public static ColorBuilder from(int color) {
        return new ColorBuilder(color);
    }

    public ColorBuilder setHue(float hue) {
        mHue = hue;
        return this;
    }

    public ColorBuilder setSaturation(float saturation) {
        mSaturation = Math.min(0.9f, Math.max(0.1f, saturation));
        return this;
    }

    public ColorBuilder setLightness(float lightness) {
        mLightness = Math.min(0.9f, Math.max(0.1f, lightness));
        return this;
    }

    public int build() {
        return ColorUtils.HSLToColor(new float[]{
                mHue, mSaturation, mLightness
        });
    }

    public float getHue() {
        return mHue;
    }

    public float getSaturation() {
        return mSaturation;
    }

    public float getLightness() {
        return mLightness;
    }
}
