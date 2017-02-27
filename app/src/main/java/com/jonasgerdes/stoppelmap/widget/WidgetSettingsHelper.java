package com.jonasgerdes.stoppelmap.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jonas on 23.02.2017.
 */

public class WidgetSettingsHelper {

    private final int mWidgetId;
    private final SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    public WidgetSettingsHelper(Context context, int widgetId) {
        mWidgetId = widgetId;
        mEditor = getPreferences(context).edit();
    }


    public static boolean getBoolean(Context context, int widgetId, String name, boolean defaultValue) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getBoolean(name + "_" + widgetId, defaultValue);
    }

    public static int getInt(Context context, int widgetId, String name, int defaultValue) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getInt(name + "_" + widgetId, defaultValue);
    }

    public static String getString(Context context, int widgetId, String name, String defaultValue) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(name + "_" + widgetId, defaultValue);
    }

    public WidgetSettingsHelper putBoolean(String name, boolean value) {
        mEditor.putBoolean(name + "_" + mWidgetId, value);
        mEditor.apply();
        return this;
    }

    public WidgetSettingsHelper putInt(String name, int value) {
        mEditor.putInt(name + "_" + mWidgetId, value);
        mEditor.apply();
        return this;
    }

    public WidgetSettingsHelper putString(String name, String value) {
        mEditor.putString(name + "_" + mWidgetId, value);
        mEditor.apply();
        return this;
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("prefs_widgets", Context.MODE_PRIVATE);
    }
}
