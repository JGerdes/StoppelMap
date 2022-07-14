package com.jonasgerdes.stoppelmap.widgets

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme

class WidgetSettingsActivity : ComponentActivity() {

    private val appWidgetId by lazy {
        intent.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StoppelMapTheme {
                Button(onClick = { addWidget() }) {
                    Text("Done")
                }
            }
        }
    }

    private fun addWidget() {
        val appWidgetId = appWidgetId ?: return

        val widgetSettings = GingerbreadWidgetSettings(this)
        val views = GingerbreadHeartWidgetProvider().initWidget(
            context = this,
            showHours = widgetSettings.getShowHour(appWidgetId, true),
            colors = arrayOf(
                widgetSettings.getColor1(appWidgetId, 0xD1C4E9),
                widgetSettings.getColor2(appWidgetId, 0x7E57C2),
                widgetSettings.getColor3(appWidgetId, 0x311B92),
            ),
        )


        //save widget
        val appWidgetManager = AppWidgetManager.getInstance(baseContext)
        appWidgetManager.updateAppWidget(appWidgetId, views)

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

}
