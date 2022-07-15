package com.jonasgerdes.stoppelmap.countdown.ui.widget.heart

import android.content.Context

private const val SHARED_PREFERENCES_NAME = "prefs_widgets"

private const val SETTING_SHOW_HOUR = "setting_show_hour"
private const val SETTING_COLOR_1 = "setting_color_1"
private const val SETTING_COLOR_2 = "setting_color_2"
private const val SETTING_COLOR_3 = "setting_color_3"

@Suppress("unused")
private const val SETTING_ACTION = "setting_action" // TODO: Implement

class GingerbreadWidgetSettings(
    context: Context
) {

    private val settings =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getShowHour(widgetId: Int, default: Boolean) =
        settings.getBoolean("$SETTING_SHOW_HOUR-$widgetId", default)

    fun getColor1(widgetId: Int, default: Int) =
        settings.getInt("$SETTING_COLOR_1-$widgetId", default)

    fun getColor2(widgetId: Int, default: Int) =
        settings.getInt("$SETTING_COLOR_2-$widgetId", default)

    fun getColor3(widgetId: Int, default: Int) =
        settings.getInt("$SETTING_COLOR_3-$widgetId", default)

}
