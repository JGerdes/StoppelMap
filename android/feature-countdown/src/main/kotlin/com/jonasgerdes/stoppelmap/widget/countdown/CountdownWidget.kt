package com.jonasgerdes.stoppelmap.widget.countdown

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.countdown.model.CountDown
import com.jonasgerdes.stoppelmap.shared.resources.Res
import com.jonasgerdes.stoppelmap.widget.glance.CustomGlanceText
import com.jonasgerdes.stoppelmap.widget.glance.StoppelMapGlanceTheme
import dev.icerock.moko.resources.PluralsResource

class CountdownWidget : GlanceAppWidget() {
    private val dependencies by lazy { CountdownWidgetDependencies() }
    private val getOpeningCountDown by lazy { dependencies.getOpeningCountDownUseCase }
    private val createStartAppIntent by lazy { dependencies.createStartAppIntentUseCase }


    companion object {
        val SIZE_SMALL = DpSize(144.dp, 144.dp)
        val SIZE_MEDIUM = DpSize(256.dp, 144.dp)
        val SIZE_LARGE = DpSize(320.dp, 144.dp)
    }

    override val sizeMode = SizeMode.Responsive(setOf(SIZE_SMALL, SIZE_MEDIUM, SIZE_LARGE))

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                WidgetContent(countDownState = getOpeningCountDown())
            }
        }
    }

    @Composable
    private fun WidgetContent(countDownState: CountDown) {
        val size = LocalSize.current
        val isWide = size.width > SIZE_SMALL.width
        Box(
            contentAlignment = Alignment.Center,
            modifier = GlanceModifier
                .size(width = size.width, height = size.height)
                .clickable(actionStartActivity(createStartAppIntent()))
        ) {
            Box(
                contentAlignment = Alignment.BottomStart,
                modifier = GlanceModifier.appWidgetBackground()
            ) {
                Image(
                    ImageProvider(R.drawable.widget_countdown_background),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = GlanceModifier.fillMaxSize()
                )
                if (!isWide) {
                    Image(
                        ImageProvider(R.drawable.jan_libett_corner),
                        contentDescription = null,
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isWide) {
                    Image(
                        ImageProvider(R.drawable.jan_libett_full),
                        contentDescription = null,
                        modifier = GlanceModifier.padding(start = 16.dp)
                    )
                }
                when (countDownState) {
                    is CountDown.InFuture -> {
                        CountdownText(
                            countDownState,
                            showHours = isWide,
                            modifier = if (isWide) GlanceModifier.padding(horizontal = 16.dp)
                            else GlanceModifier.padding(start = 48.dp),
                            footer = {
                                if (isWide) {
                                    CustomGlanceText(
                                        text = LocalContext.current.getString(
                                            R.string.widget_countdown_footer,
                                            countDownState.year
                                        ),
                                        style = StoppelMapGlanceTheme.typography.subTitle
                                    )
                                } else {
                                    CustomGlanceText(
                                        text = LocalContext.current.getString(
                                            R.string.widget_countdown_footer_noYear
                                        ),
                                        style = StoppelMapGlanceTheme.typography.body
                                    )
                                    CustomGlanceText(
                                        text = countDownState.year.toString(),
                                        style = StoppelMapGlanceTheme.typography.body
                                    )
                                }
                            }
                        )
                    }

                    is CountDown.OnGoing -> {
                        Text("OnGoing")
                    }
                }
            }
        }
    }


    @Composable
    private fun CountdownText(
        countDownState: CountDown.InFuture,
        showHours: Boolean,
        footer: @Composable () -> Unit,
        modifier: GlanceModifier,
    ) {
        Column(modifier = modifier) {
            val context = LocalContext.current
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                modifier = GlanceModifier
                    .height((3 * (144 / 4)).dp)
                    .fillMaxWidth()
            ) {
                CustomGlanceText(
                    text = context.getString(R.string.widget_countdown_prefix),
                    style = StoppelMapGlanceTheme.typography.body
                )
                Row(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CountdownItem(
                        unitString = Res.plurals.countdownCard_unit_day,
                        value = countDownState.daysLeft,
                    )
                    if (showHours) {
                        CountdownItem(
                            unitString = Res.plurals.countdownCard_unit_hour,
                            value = countDownState.hoursLeft,
                        )
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                modifier = GlanceModifier.height((144 / 4).dp)
                    .fillMaxWidth()
            ) {
                footer()
            }
        }
    }
}

@Composable
fun CountdownItem(unitString: PluralsResource, value: Int) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
        modifier = GlanceModifier.padding(horizontal = 8.dp)
    ) {
        CustomGlanceText(
            text = value.toString(),
            style = StoppelMapGlanceTheme.typography.title
        )
        CustomGlanceText(
            text = unitString.getQuantityString(
                context,
                value
            ),
            style = StoppelMapGlanceTheme.typography.body
        )
    }
}