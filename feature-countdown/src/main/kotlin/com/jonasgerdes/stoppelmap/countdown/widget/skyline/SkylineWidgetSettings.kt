package com.jonasgerdes.stoppelmap.countdown.widget.skyline

import android.content.SharedPreferences
import androidx.core.content.edit
import com.jonasgerdes.stoppelmap.countdown.ui.Font

private const val SETTING_SHOW_HOUR = "setting_show_hour"
private const val SETTING_COLOR = "setting_color"
private const val SETTING_FONT_COLOR = "setting_font_color"
private const val SETTING_FONT = "setting_font"


@Suppress("unused")
private const val SETTING_ACTION = "setting_action" // TODO: Implement

data class SkylineWidgetSettings(
    val appWidgetId: Int,
    val showHours: Boolean = DEFAULT_SHOW_HOUR,
    val color: Int = DEFAULT_COLOR,
    val fontColor: Int = DEFAULT_FONT_COLOR,
    val font: Font = DEFAULT_FONT,
    val action: Action = DEFAULT_ACTION
) {
    enum class Action {
        OPEN_SETTINGS
    }

    companion object {
        const val DEFAULT_SHOW_HOUR = false
        const val DEFAULT_COLOR = 0x0b0b0b
        const val DEFAULT_FONT_COLOR = 0xf4f4f4
        val DEFAULT_FONT = Font.RobotoSlab
        val DEFAULT_ACTION = Action.OPEN_SETTINGS


        fun saveToPreferences(
            preferences: SharedPreferences,
            widgetSettings: SkylineWidgetSettings
        ) = with(widgetSettings) {
            preferences.edit {
                putBoolean("$SETTING_SHOW_HOUR-$appWidgetId", showHours)
                putInt("$SETTING_COLOR-$appWidgetId", color)
                putInt("$SETTING_FONT_COLOR-$appWidgetId", fontColor)
                putString("$SETTING_FONT-$appWidgetId", font.originalAsset)
                putString("$SETTING_ACTION-$appWidgetId", action.name)
            }
        }

        fun loadFromPreferences(
            preferences: SharedPreferences,
            appWidgetId: Int,
        ) = with(preferences) {
            SkylineWidgetSettings(
                appWidgetId = appWidgetId,
                showHours = getBoolean(
                    "$SETTING_SHOW_HOUR-$appWidgetId",
                    DEFAULT_SHOW_HOUR
                ),
                color = getInt("$SETTING_COLOR-$appWidgetId", DEFAULT_COLOR),
                fontColor = getInt("$SETTING_FONT_COLOR-$appWidgetId", DEFAULT_FONT_COLOR),
                font = getString("$SETTING_FONT-$appWidgetId", null)
                    .let { storedValue ->
                        Font.values().firstOrNull { it.originalAsset == storedValue }
                            ?: DEFAULT_FONT
                    },
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
