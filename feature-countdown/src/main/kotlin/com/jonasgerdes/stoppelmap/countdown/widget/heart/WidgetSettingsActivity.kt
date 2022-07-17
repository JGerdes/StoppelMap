@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPagerApi::class,
    ExperimentalAnimationApi::class
)

package com.jonasgerdes.stoppelmap.countdown.widget.heart

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.countdown.ui.components.settings.ColorSettingsCard
import com.jonasgerdes.stoppelmap.countdown.ui.components.settings.ShowHoursSettingsCard
import com.jonasgerdes.stoppelmap.countdown.widget.mutablePreferenceStateOf
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber


class WidgetSettingsActivity : ComponentActivity() {

    private var appWidgetId by mutableStateOf(-1)
    private val sharedPreferences: SharedPreferences by inject()

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Timber.d(
            "onNewIntent: ${intent}, ${
                intent.extras?.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
                )
            }"
        )
        appWidgetId = intent.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d(
            "onCreate: ${intent}, ${
                intent.extras?.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
                )
            }"
        )
        super.onCreate(savedInstanceState)
        appWidgetId = intent.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )!!
        setContent {
            StoppelMapTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = stringResource(id = R.string.widget_configuration_title) + " #$appWidgetId") },
                        )
                    }
                ) { scaffoldPadding ->
                    var widetSettings by remember(appWidgetId) {
                        mutablePreferenceStateOf(
                            sharedPreferences,
                            GingerbreadWidgetSettings.loadFromPreferences(appWidgetId),
                            GingerbreadWidgetSettings::saveToPreferences
                        )
                    }

                    Box(Modifier.fillMaxSize()) {
                        CheckerBoxBackground(Modifier.fillMaxSize())
                        Column(
                            Modifier
                                .padding(scaffoldPadding)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Spacer(modifier = Modifier.size(16.dp))
                            WidgetPreview(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                settings = widetSettings
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            WidgetSettingsPager(
                                onSave = { updateWidget() },
                                settingsCards = listOf(
                                    {
                                        ColorSettingsCard(
                                            colors = listOf(
                                                widetSettings.color1,
                                                widetSettings.color2,
                                                widetSettings.color3
                                            ),
                                            onColorsChanged = {
                                                widetSettings = widetSettings.copy(
                                                    color1 = it[0],
                                                    color2 = it[1],
                                                    color3 = it[2],
                                                )
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    },
                                    {
                                        ShowHoursSettingsCard(
                                            showHours = widetSettings.showHours,
                                            onShowHoursChanged = {
                                                widetSettings = widetSettings.copy(showHours = it)
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                )
                            )

                        }

                    }
                }
            }
        }
    }

    private fun updateWidget() {
        val views = GingerbreadHeartWidgetProvider().initWidget(
            context = this,
            settings = GingerbreadWidgetSettings.loadFromPreferences(appWidgetId)(sharedPreferences)
        )

        val appWidgetManager = AppWidgetManager.getInstance(baseContext)
        appWidgetManager.updateAppWidget(appWidgetId, views)

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

}

@Composable
fun WidgetSettingsPager(
    onSave: () -> Unit,
    settingsCards: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val scrollToNextPage = {
        scope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }
    val isLastPage = pagerState.currentPage == pagerState.pageCount - 1
    Box(modifier = modifier) {
        Column {
            Spacer(modifier = Modifier.size(16.dp))
            HorizontalPager(
                count = settingsCards.size,
                state = pagerState,
                contentPadding = PaddingValues(16.dp),
                itemSpacing = 16.dp,
                verticalAlignment = Alignment.Bottom
            ) { page ->
                settingsCards[page]()
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp)
        ) {
            AnimatedVisibility(
                visible = !pagerState.isScrollInProgress,
                enter = fadeIn() + scaleIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                FloatingActionButton(
                    onClick = {
                        if (isLastPage) onSave() else scrollToNextPage()
                    },
                    shape = Shapes.Full,
                ) {
                    if (isLastPage) {
                        Icon(
                            Icons.Rounded.Save,
                            contentDescription = stringResource(
                                id = R.string.widget_configuration_button_save_contentDescription
                            )
                        )
                    } else {
                        Icon(
                            Icons.Rounded.ArrowForward,
                            contentDescription = stringResource(
                                id = R.string.widget_configuration_button_next_contentDescription
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WidgetPreview(modifier: Modifier, settings: GingerbreadWidgetSettings) {
    val widgetProvider = remember { GingerbreadHeartWidgetProvider() }
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            FrameLayout(context)
        },
        update = {
            it.removeAllViews()
            val views = widgetProvider.initWidget(context, settings)
            it.addView(views.apply(context, it))
        }
    )
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
