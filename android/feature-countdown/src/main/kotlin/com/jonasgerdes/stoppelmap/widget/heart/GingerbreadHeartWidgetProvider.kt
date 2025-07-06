package com.jonasgerdes.stoppelmap.widget.heart

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
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
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownUseCase
import com.jonasgerdes.stoppelmap.countdown.widget.heart.GingerbreadWidgetSettings
import com.jonasgerdes.stoppelmap.widget.CreateStartAppIntentUseCase
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.roundToInt
import androidx.core.graphics.createBitmap

class GingerbreadHeartWidgetProvider : AppWidgetProvider() {

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
        val settings = GingerbreadWidgetSettings.loadFromPreferences(sharedPreferences, appWidgetId)
        val views = initWidget(
            context = context,
            settings = settings,
        )
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    internal fun initWidget(
        context: Context,
        settings: GingerbreadWidgetSettings,
    ) = RemoteViews(
        context.packageName,
        R.layout.widget_layout_gingerbread_heart
    ).apply {
        val views = this
        val size = Point(256.dp.toPx(context), 206.dp.toPx(context))
        val countdownBitmap = createCountdownBitmap(
            context,
            getCountdownTexts(context.resources, settings.showHours),
            size,
            settings.showHours
        )
        views.setImageViewBitmap(R.id.widget_countdown, countdownBitmap)
        views.setInt(R.id.widget_gingerbread_heart_layer1, "setColorFilter", settings.color1)
        views.setInt(R.id.widget_gingerbread_heart_layer2, "setColorFilter", settings.color2)
        views.setInt(R.id.widget_gingerbread_heart_layer3, "setColorFilter", settings.color3)

        val intent: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                createStartAppIntent(),
                PendingIntent.FLAG_IMMUTABLE
            )

        views.setOnClickPendingIntent(R.id.widget_countdown, intent)
        return views
    }

    private fun getCountdownTexts(
        resources: Resources,
        showHours: Boolean
    ): CountdownTexts = with(resources) {
        when (val countdown = getTimeLeftToOpening()) {
            is CountDown.InFuture -> with(countdown) {
                CountdownTexts(
                    mainText = listOfNotNull(
                        if (countdown.daysLeft > 0) getQuantityString(
                            R.plurals.countdownWidget_day, daysLeft, daysLeft
                        ) else null,
                        if (countdown.daysLeft == 0 || showHours) getQuantityString(
                            R.plurals.countdownWidget_hour, hoursLeft, hoursLeft
                        ) else null,
                    ).joinToString(", "),
                    preposition = getString(R.string.countdownWidget_preposition_until),
                    year = countdown.season.year.toString()
                )
            }

            is CountDown.OnGoing -> CountdownTexts(
                mainText = getString(R.string.countdownWidget_mainText_ongoing),
                preposition = getString(R.string.countdownWidget_preposition_ongoing),
                year = countdown.season.year.toString()
            )
        }
    }
}

private data class CountdownTexts(
    val mainText: String,
    val preposition: String,
    val year: String,
)

private val textBounds by lazy { Rect() }

private fun createCountdownBitmap(
    context: Context?,
    texts: CountdownTexts,
    size: Point,
    showHours: Boolean
): Bitmap {
    val bitmap = createBitmap(size.x, size.y)
    val canvas = Canvas(bitmap)
    var countDownSize = size.y / 9f
    if (!showHours) {
        countDownSize = size.y / 5f
    }
    val font = ResourcesCompat.getFont(context!!, R.font.damion)
    val paint = Paint().apply {
        isAntiAlias = true
        isSubpixelText = true
        typeface = font
        style = Paint.Style.FILL
        color = Color.rgb(244, 244, 244)
        textSize = countDownSize
    }

    paint.getTextBounds(texts.mainText, 0, texts.mainText.length, textBounds)
    canvas.drawText(
        texts.mainText,
        size.x / 2f - textBounds.exactCenterX(),
        size.y * 0.4f,
        paint
    )
    paint.textSize = size.y / 14f
    paint.textAlign = Paint.Align.RIGHT
    canvas.drawText(texts.preposition, size.x / 2f, size.y * 0.55f, paint)
    paint.textSize = size.y / 14f
    paint.textAlign = Paint.Align.LEFT
    canvas.drawText(texts.year, size.x * 0.53f, size.y * 0.7f, paint)
    return bitmap
}

private fun Dp.toPx(context: Context) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics)
        .roundToInt()
