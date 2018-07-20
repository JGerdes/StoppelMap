package com.jonasgerdes.stoppelmap.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;

import com.jonasgerdes.stoppelmap.R;

public enum Font {
    //legacy wrapper - in old versions font is load from assets and file name was
    //saved in shared prefs
    Roboto("Roboto-Thin.ttf", R.font.roboto_thin),
    RobotoSlab("RobotoSlab-Light.ttf", R.font.roboto_slab_light),
    Damion("Damion-Regular.ttf", R.font.damion);

    private final String fileName;
    private int fontRes;

    Font(String fileName, @FontRes int fontRes) {
        this.fileName = fileName;
        this.fontRes = fontRes;
    }

    @NonNull
    public String getFileName() {
        return fileName;
    }

    @Nullable
    public static Font fromStringOrNull(String fileName) {
        for (Font font : values()) {
            if (font.fileName.equals(fileName)) {
                return font;
            }
        }
        return null;
    }

    public Typeface asTypeface(Context context) {
        return ResourcesCompat.getFont(context, fontRes);
    }

}