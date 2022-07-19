package com.jonasgerdes.stoppelmap.countdown.widget.heart

import android.content.SharedPreferences
import androidx.core.content.edit

private const val SETTING_SHOW_HOUR = "setting_show_hour"
private const val SETTING_COLOR_1 = "setting_color_1"
private const val SETTING_COLOR_2 = "setting_color_2"
private const val SETTING_COLOR_3 = "setting_color_3"


@Suppress("unused")
private const val SETTING_ACTION = "setting_action" // TODO: Implement

data class GingerbreadWidgetSettings(
    val appWidgetId: Int,
    val showHours: Boolean = DEFAULT_SHOW_HOUR,
    val color1: Int = DEFAULT_COLOR_1,
    val color2: Int = DEFAULT_COLOR_2,
    val color3: Int = DEFAULT_COLOR_3,
    val action: Action = DEFAULT_ACTION
) {
    enum class Action {
        OPEN_SETTINGS
    }

    companion object {
        const val DEFAULT_SHOW_HOUR = false
        const val DEFAULT_COLOR_1 = 0xD1C4E9
        const val DEFAULT_COLOR_2 = 0x7E57C2
        const val DEFAULT_COLOR_3 = 0x311B92
        val DEFAULT_ACTION = Action.OPEN_SETTINGS


        fun saveToPreferences(
            preferences: SharedPreferences,
            widgetSettings: GingerbreadWidgetSettings
        ) = with(widgetSettings) {
            preferences.edit {
                putBoolean("$SETTING_SHOW_HOUR-$appWidgetId", showHours)
                putInt("$SETTING_COLOR_1-$appWidgetId", color1)
                putInt("$SETTING_COLOR_2-$appWidgetId", color2)
                putInt("$SETTING_COLOR_3-$appWidgetId", color3)
                putString("$SETTING_ACTION-$appWidgetId", action.name)
            }
        }

        fun loadFromPreferences(
            preferences: SharedPreferences,
            appWidgetId: Int,
        ) = with(preferences) {
            GingerbreadWidgetSettings(
                appWidgetId = appWidgetId,
                showHours = getBoolean(
                    "$SETTING_SHOW_HOUR-$appWidgetId",
                    DEFAULT_SHOW_HOUR
                ),
                color1 = getInt("$SETTING_COLOR_1-$appWidgetId", DEFAULT_COLOR_1),
                color2 = getInt("$SETTING_COLOR_2-$appWidgetId", DEFAULT_COLOR_2),
                color3 = getInt("$SETTING_COLOR_3-$appWidgetId", DEFAULT_COLOR_3),
                action = try {
                    Action.valueOf(
                        getString("$SETTING_ACTION-$appWidgetId", null)
                            ?: DEFAULT_ACTION.name
                    )
                } catch (invalidValue: IllegalArgumentException) {
                    DEFAULT_ACTION
                },
            )
        }
    }
}
