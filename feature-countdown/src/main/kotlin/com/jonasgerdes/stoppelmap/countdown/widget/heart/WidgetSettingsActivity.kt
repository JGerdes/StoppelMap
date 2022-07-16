@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPagerApi::class,
    ExperimentalAnimationApi::class
)

package com.jonasgerdes.stoppelmap.countdown.widget.heart

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
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
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import kotlinx.coroutines.launch
import timber.log.Timber


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
                                showHours = false,
                                colors = arrayOf(0xD1C4E9, 0x7E57C2, 0x311B92)
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            WidgetSettingsPager(
                                onSave = { addWidget() },
                                settingsCards = listOf(
                                    { ColorSettingsCard(modifier = Modifier.fillMaxWidth()) },
                                    { ColorSettingsCard(modifier = Modifier.fillMaxWidth()) }
                                )
                            )

                        }

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
        Timber.d("finish")
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
    Box {
        Column {
            Spacer(modifier = Modifier.size(28.dp))
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
fun ColorSettingsCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)) {
        Column(Modifier.padding(16.dp)) {
            var checkBoxState by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkBoxState,
                    onCheckedChange = { checkBoxState = it }
                )
                Text(text = "Zeige Tage")
            }
        }
    }
}

@Composable
fun WidgetPreview(modifier: Modifier, showHours: Boolean, colors: Array<Int>) {
    val widgetProvider = remember { GingerbreadHeartWidgetProvider() }
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            FrameLayout(context)
        },
        update = {
            it.removeAllViews()
            val views = widgetProvider.initWidget(context, 0, showHours, colors)
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
