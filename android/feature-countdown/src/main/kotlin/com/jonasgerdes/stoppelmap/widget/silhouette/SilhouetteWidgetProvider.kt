package com.jonasgerdes.stoppelmap.widget.silhouette

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.countdown.model.CountDown
import com.jonasgerdes.stoppelmap.countdown.ui.Font
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownUseCase
import com.jonasgerdes.stoppelmap.countdown.widget.skyline.SkylineWidgetSettings
import com.jonasgerdes.stoppelmap.widget.CreateStartAppIntentUseCase
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.roundToInt

class SilhouetteWidgetProvider : AppWidgetProvider() {

    private val sharedPreferences: SharedPreferences by inject(SharedPreferences::class.java)
    private val createStartAppIntent: CreateStartAppIntentUseCase by inject(
        CreateStartAppIntentUseCase::class.java
    )

    private val getTimeLeftToOpening: GetOpeningCountDownUseCase by inject(
        GetOpeningCountDownUseCase::class.java
    )

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach {
            updateWidget(context, appWidgetManager, it)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        updateWidget(context, appWidgetManager, appWidgetId)
    }

    fun updateAllWidgets(
        context: Context,
        appWidgetManager: AppWidgetManager
    ) {
        appWidgetManager.getAppWidgetIds(
            ComponentName(
                context.packageName,
                this::class.java.name
            )
        ).forEach {
            updateWidget(context, appWidgetManager, it)
        }
    }

    fun getWidgetCount(
        context: Context,
        appWidgetManager: AppWidgetManager
    ) = appWidgetManager.getAppWidgetIds(
        ComponentName(
            context.packageName,
            this::class.java.name
        )
    ).size

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val settings = SkylineWidgetSettings.loadFromPreferences(sharedPreferences, appWidgetId)
        val views = initWidget(
            context = context,
            settings = settings,
        )
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    internal fun initWidget(
        context: Context,
        settings: SkylineWidgetSettings,
    ) = RemoteViews(
        context.packageName,
        R.layout.widget_layout_skyline
    ).apply {
        val views = this
        val size = Point(256.dp.toPx(context), 206.dp.toPx(context))
        val countdownBitmap = createCountdownBitmap(
            context,
            getCountdownTexts(settings.showHours, context.resources),
            size,
            settings.showHours,
            settings.font,
            settings.fontColor
        )
        views.setImageViewBitmap(R.id.widget_countdown, countdownBitmap)
        views.setInt(R.id.widget_skyline, "setColorFilter", settings.color)

        val intent: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                createStartAppIntent(),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE
                } else 0
            )

        views.setOnClickPendingIntent(R.id.widget_countdown, intent)
        return views
    }

    private fun getCountdownTexts(
        showHours: Boolean,
        resources: Resources
    ): CountdownTexts = with(resources) {
        when (val countdown = getTimeLeftToOpening()) {
            is CountDown.InFuture -> with(countdown) {
                CountdownTexts(
                    mainText = when {
                        showHours -> getQuantityString(
                            R.plurals.countdownWidget_day,
                            daysLeft,
                            daysLeft
                        )
                        // Switch to hours anyway if hours are not shown
                        daysLeft == 0 -> getQuantityString(
                            R.plurals.countdownWidget_hour,
                            hoursLeft,
                            hoursLeft
                        )

                        else -> getQuantityString(
                            R.plurals.countdownWidget_day,
                            daysLeft,
                            daysLeft
                        )
                    },
                    subText = getQuantityString(
                        R.plurals.countdownWidget_hour,
                        hoursLeft,
                        hoursLeft
                    ),
                    preposition = getString(R.string.countdownWidget_preposition_still)
                )
            }

            is CountDown.OnGoing -> CountdownTexts(
                mainText = getString(R.string.countdownWidget_mainText_ongoing),
                subText = "",
                preposition = getString(R.string.countdownWidget_preposition_still)
            )
        }
    }
}

private data class CountdownTexts(
    val mainText: String,
    val subText: String,
    val preposition: String,
)

private val textBounds by lazy { Rect() }

private fun createCountdownBitmap(
    context: Context,
    texts: CountdownTexts,
    size: Point,
    showHours: Boolean,
    font: Font,
    fontColor: Int
): Bitmap {
    val bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val centerOffset = 0.065f * size.y
    var countDownSize = size.y / 10f
    var daysYPosition = size.y * 0.7f
    if (!showHours) {
        daysYPosition = size.y * 0.76f
        countDownSize = size.y / 6.5f
    }

    if (font == Font.Damion) {
        countDownSize *= 1.2f
    }

    val paint = Paint().apply {
        typeface = ResourcesCompat.getFont(context, font.fontResource)
        isAntiAlias = true
        isSubpixelText = true
        style = Paint.Style.FILL
        color = fontColor
    }

    paint.textSize = size.y / 15f

    paint.getTextBounds(texts.preposition, 0, texts.preposition.length, textBounds)
    canvas.drawText(
        texts.preposition, size.x / 2f - textBounds.exactCenterX() + centerOffset,
        size.y * 0.55f, paint
    )

    paint.textSize = countDownSize
    paint.getTextBounds(texts.mainText, 0, texts.mainText.length, textBounds)
    canvas.drawText(
        texts.mainText, size.x / 2f - textBounds.exactCenterX() + centerOffset,
        daysYPosition, paint
    )

    if (showHours) {
        paint.textSize = size.y / 12f
        paint.getTextBounds(texts.subText, 0, texts.subText.length, textBounds)
        canvas.drawText(
            texts.subText, size.x / 2f - textBounds.exactCenterX() + centerOffset,
            size.y * 0.8f, paint
        )
    }
    return bitmap
}

private fun Dp.toPx(context: Context) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics)
        .roundToInt()
