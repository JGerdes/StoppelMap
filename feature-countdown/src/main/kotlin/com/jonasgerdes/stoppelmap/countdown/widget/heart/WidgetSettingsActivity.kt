@file:OptIn(ExperimentalMaterial3Api::class)

package com.jonasgerdes.stoppelmap.countdown.widget.heart

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.jonasgerdes.stoppelmap.countdown.R
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
                Scaffold(
                    topBar = { CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.widget_configuration_title)) }) }
                ) { scaffoldPadding ->
                    Box(Modifier.fillMaxSize()) {
                        CheckerBoxBackground(Modifier.fillMaxSize())

                    }
                }
            }
        }
    }

    private fun addWidget() {
        val appWidgetId = appWidgetId ?: return

        val widgetSettings = GingerbreadWidgetSettings(this)
        val views = GingerbreadHeartWidgetProvider().initWidget(
            context = this,
            appWidgetId = appWidgetId,
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

@Composable
fun CheckerBoxBackground(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Canvas(
        modifier = modifier
    ) {
        val pattern =
            context.getBitmapFromVectorDrawable(R.drawable.checkerbox_tile).asImageBitmap()

        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            shader = ImageShader(pattern, TileMode.Repeated, TileMode.Repeated)
        }

        drawIntoCanvas {
            it.nativeCanvas.drawPaint(paint)
        }
        paint.reset()
    }

}

private fun Context.getBitmapFromVectorDrawable(drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(this, drawableId)!!
    val bitmap: Bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
