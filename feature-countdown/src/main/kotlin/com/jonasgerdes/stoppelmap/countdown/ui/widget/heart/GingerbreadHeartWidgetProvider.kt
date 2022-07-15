package com.jonasgerdes.stoppelmap.countdown.ui.widget.heart

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.res.Resources
import android.graphics.*
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
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.roundToInt

class GingerbreadHeartWidgetProvider : AppWidgetProvider() {

    private val widgetSettings: GingerbreadWidgetSettings by inject(GingerbreadWidgetSettings::class.java)
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

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = initWidget(
            context = context,
            showHours = widgetSettings.getShowHour(appWidgetId, true),
            colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                arrayOf(
                    context.resources.getColor(android.R.color.system_accent1_300, context.theme),
                    context.resources.getColor(android.R.color.system_accent1_200, context.theme),
                    context.resources.getColor(android.R.color.system_accent1_100, context.theme),
                )
            } else arrayOf(
                widgetSettings.getColor1(appWidgetId, 0xD1C4E9),
                widgetSettings.getColor2(appWidgetId, 0x7E57C2),
                widgetSettings.getColor3(appWidgetId, 0x311B92),
            ),
        )
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    internal fun initWidget(
        context: Context,
        showHours: Boolean,
        colors: Array<Int>
    ) = RemoteViews(
        context.packageName,
        R.layout.widget_layout_gingerbread_heart
    ).apply {
        val views = this
        val size = Point(256.dp.toPx(context), 206.dp.toPx(context))
        val countdownBitmap = createCountdownBitmap(
            context,
            getCountdownTexts(context.resources, showHours),
            size,
            showHours
        )
        views.setImageViewBitmap(R.id.widget_countdown, countdownBitmap)
        views.setInt(R.id.widget_gingerbread_heart_layer1, "setColorFilter", colors[0])
        views.setInt(R.id.widget_gingerbread_heart_layer2, "setColorFilter", colors[1])
        views.setInt(R.id.widget_gingerbread_heart_layer3, "setColorFilter", colors[2])
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
                    year = "2022"
                )
            }
            CountDown.InPast -> CountdownTexts(
                mainText = getString(R.string.countdownWidget_mainText_past),
                preposition = getString(R.string.countdownWidget_preposition_past),
                year = "2023"
            )
            CountDown.OnGoing -> CountdownTexts(
                mainText = getString(R.string.countdownWidget_mainText_ongoing),
                preposition = getString(R.string.countdownWidget_preposition_ongoing),
                year = "2022"
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
    val bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888)
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
