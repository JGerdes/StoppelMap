package com.jonasgerdes.stoppelmap.countdown.usecase

import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Build
import com.jonasgerdes.stoppelmap.widget.heart.GingerbreadHeartWidgetProvider
import com.jonasgerdes.stoppelmap.widget.silhouette.SilhouetteWidgetProvider

class ShouldShowCountdownWidgetSuggestionUseCase(
    private val gingerbreadHeartWidgetProvider: GingerbreadHeartWidgetProvider,
    private val skylineWidgetProvider: SilhouetteWidgetProvider,
    private val context: Context,
    private val appWidgetManager: AppWidgetManager,
) {

    operator fun invoke(): Boolean =
        when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> false
            gingerbreadHeartWidgetProvider.getWidgetCount(context, appWidgetManager) > 0 -> false
            skylineWidgetProvider.getWidgetCount(context, appWidgetManager) > 0 -> false
            // TODO: store last shown date and only show once in a while
            else -> true
        }
}
