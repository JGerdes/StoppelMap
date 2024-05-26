package com.jonasgerdes.stoppelmap.countdown.usecase

import android.appwidget.AppWidgetManager
import android.content.Context
import com.jonasgerdes.stoppelmap.widget.countdown.CountdownWidget
import com.jonasgerdes.stoppelmap.widget.glance.getWidgetCount
import com.jonasgerdes.stoppelmap.widget.heart.GingerbreadHeartWidgetProvider
import com.jonasgerdes.stoppelmap.widget.silhouette.SilhouetteWidgetProvider
import timber.log.Timber

class ShouldShowCountdownWidgetSuggestionUseCaseImpl(
    private val gingerbreadHeartWidgetProvider: GingerbreadHeartWidgetProvider,
    private val skylineWidgetProvider: SilhouetteWidgetProvider,
    private val context: Context,
    private val appWidgetManager: AppWidgetManager,
) : ShouldShowCountdownWidgetSuggestionUseCase {

    override suspend operator fun invoke(): Boolean =
        when {
            gingerbreadHeartWidgetProvider.getWidgetCount(context, appWidgetManager) > 0 -> false
            skylineWidgetProvider.getWidgetCount(context, appWidgetManager) > 0 -> false
            CountdownWidget().getWidgetCount(context) > 0 -> false
            // TODO: store last shown date and only show once in a while
            else -> true
        }.also {
            Timber.d(
                """
                Widget counts:
                heart: ${gingerbreadHeartWidgetProvider.getWidgetCount(context, appWidgetManager)}
                skyline: ${skylineWidgetProvider.getWidgetCount(context, appWidgetManager)}
                countdown: ${CountdownWidget().getWidgetCount(context)}
            """.trimIndent()
            )
        }
}
