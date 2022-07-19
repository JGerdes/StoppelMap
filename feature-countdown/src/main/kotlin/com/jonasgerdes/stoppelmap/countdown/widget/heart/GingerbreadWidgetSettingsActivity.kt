@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPagerApi::class,
    ExperimentalAnimationApi::class, ExperimentalPagerApi::class, ExperimentalPagerApi::class,
    ExperimentalLifecycleComposeApi::class
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.countdown.ui.components.settings.ColorSettingsCard
import com.jonasgerdes.stoppelmap.countdown.ui.components.settings.ShowHoursSettingsCard
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class WidgetSettingsActivity : ComponentActivity() {

    private val sharedPreferences: SharedPreferences by inject()
    private val viewModel: GingerbreadWidgetSettingsViewModel by viewModel()

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewModel.onAppWidgetIdChanged(
            appWidgetId = intent.extras?.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )!!
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAppWidgetIdChanged(
            appWidgetId = intent.extras?.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )!!
        )
        setContent {
            StoppelMapTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()

                (state as? GingerbreadWidgetSettingsViewModel.ViewState.Done)?.run {
                    saveWidgetAndFinish(settings)
                }

                val appWidgetId =
                    (state as? GingerbreadWidgetSettingsViewModel.ViewState.Loaded)?.appWidgetId
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = stringResource(id = R.string.widget_configuration_title) + " #$appWidgetId") },
                        )
                    }
                ) { scaffoldPadding ->
                    WidgetSettingsScreen(
                        state = state,
                        onShowHoursChange = viewModel::onShowHoursChanged,
                        onHueChange = viewModel::onHueChanged,
                        onSaturationChange = viewModel::onSaturationChanged,
                        onBrightnessChange = viewModel::onBrightnessChanged,
                        onColorSelect = viewModel::onColorSelected,
                        onSaveTap = viewModel::onSaveSettingsTapped,
                        modifier = Modifier
                            .padding(scaffoldPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }

    private fun saveWidgetAndFinish(settings: GingerbreadWidgetSettings) {
        val views = GingerbreadHeartWidgetProvider().initWidget(
            context = this,
            settings = settings
        )
        GingerbreadWidgetSettings.saveToPreferences(sharedPreferences, settings)

        val appWidgetManager = AppWidgetManager.getInstance(baseContext)
        appWidgetManager.updateAppWidget(settings.appWidgetId, views)

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, settings.appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

}

@Composable
fun WidgetSettingsScreen(
    state: GingerbreadWidgetSettingsViewModel.ViewState,
    onShowHoursChange: (Boolean) -> Unit,
    onHueChange: (Float) -> Unit,
    onSaturationChange: (Float) -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onColorSelect: (Int) -> Unit,
    onSaveTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CheckerBoxBackground(Modifier.fillMaxSize())
        if (state is GingerbreadWidgetSettingsViewModel.ViewState.Loaded) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.size(16.dp))
                WidgetPreview(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    settings = state.settings,
                )
                Spacer(modifier = Modifier.size(16.dp))
                WidgetSettingsPager(
                    onSave = onSaveTap,
                    settingsCards = listOf(
                        {
                            ColorSettingsCard(
                                hue = state.colorSettings.hue,
                                saturation = state.colorSettings.saturation,
                                brightness = state.colorSettings.brightness,
                                onHueChanged = onHueChange,
                                onSaturationChanged = onSaturationChange,
                                onBrightnessChanged = onBrightnessChange,
                                onColorChanged = onColorSelect,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        },
                        {
                            ShowHoursSettingsCard(
                                showHours = state.displaySettings.showHours,
                                onShowHoursChanged = onShowHoursChange,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                )

            }

        }
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
