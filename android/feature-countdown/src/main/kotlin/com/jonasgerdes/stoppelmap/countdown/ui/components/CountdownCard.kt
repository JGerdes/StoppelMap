package com.jonasgerdes.stoppelmap.countdown.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.PluralsRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import androidx.constraintlayout.compose.Dimension.Companion.wrapContent
import com.jonasgerdes.stoppelmap.base.contract.Season
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.resources.dayOfMonthFormat
import com.jonasgerdes.stoppelmap.resources.defaultFormat
import com.jonasgerdes.stoppelmap.theme.LocalThemeSetting
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import com.jonasgerdes.stoppelmap.theme.isDarkTheme
import com.jonasgerdes.stoppelmap.theme.modifier.invisible
import com.jonasgerdes.stoppelmap.theme.settings.ThemeSetting
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

@Composable
fun CountdownCard(
    days: Int,
    hours: Int,
    minutes: Int,
    seconds: Int,
    season: Season,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = LocalThemeSetting.current.isDarkTheme()
    Card(modifier = modifier) {
        // We want the illustration next to the countdown and info,
        // the background colors matching the height of the countdown and info
        // while also reaching beneath the illustration.
        ConstraintLayout(modifier.fillMaxWidth()) {
            val (barrierHelper, skyBackground, fieldBackground,
                illustration, countdown, footer) = createRefs()

            val illustrationGuideline = createGuidelineFromStart(128.dp)
            val illustrationBarrier = createStartBarrier(countdown, barrierHelper)

            // This is just here so we can constraint the barrier to the the guideline,
            // which isn't supported by ConstraintLayout out-of-the-box.
            Box(Modifier.constrainAs(barrierHelper) {
                top.linkTo(parent.top)
                start.linkTo(illustrationGuideline)
                end.linkTo(illustrationGuideline)
            })
            Box(
                Modifier
                    .background(
                        if (isDarkTheme) MaterialTheme.colorScheme.surfaceContainer
                        else colorResource(id = R.color.stoppelsky)
                    )
                    .constrainAs(skyBackground) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(countdown.bottom)
                        width = fillToConstraints
                        height = fillToConstraints
                    }
            )
            Box(
                Modifier
                    .background(
                        if (isDarkTheme) MaterialTheme.colorScheme.surfaceContainerHigh
                        else colorResource(id = R.color.stoppelfield)
                    )
                    .constrainAs(fieldBackground) {
                        top.linkTo(countdown.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        width = fillToConstraints
                        height = fillToConstraints
                    }
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .constrainAs(countdown) {
                        start.linkTo(illustrationGuideline)
                        // Double padding of 16.dp to the end to have some additional
                        // wiggle room for large font scaling.
                        end.linkTo(parent.end, 16.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(footer.top)
                        width = wrapContent
                    }
                    .width(IntrinsicSize.Min)
                    .padding(vertical = 16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CountdownUnit(
                        unitLabel = R.plurals.countdownCard_unit_day,
                        value = days,
                        modifier = Modifier.weight(1f)
                    )
                    CountdownUnit(
                        unitLabel = R.plurals.countdownCard_unit_hour,
                        value = hours,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CountdownUnit(
                        unitLabel = R.plurals.countdownCard_unit_minute,
                        value = minutes,
                        modifier = Modifier.weight(1f)
                    )
                    CountdownUnit(
                        unitLabel = R.plurals.countdownCard_unit_second,
                        value = seconds,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .constrainAs(footer) {
                        top.linkTo(countdown.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = wrapContent
                        width = wrapContent
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(
                        id = R.string.countdownCard_suffix,
                        season.year
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                )
                Text(
                    text = stringResource(
                        id = R.string.countdownCard_iteration,
                        season.iteration,
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.End,
                )
                val context = LocalContext.current
                Text(
                    text = stringResource(
                        id = R.string.countdownCard_dates,
                        season.start.date.dayOfMonthFormat().toString(context),
                        season.end.date.defaultFormat().toString(context),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                )
            }
            Image(
                painter = painterResource(R.drawable.jan_libett_balloons),
                contentDescription = null,
                colorFilter = if (isDarkTheme) {
                    // Darkens the illustration a little bit, so we don't need sunglasses to look at it.
                    ColorFilter.lighting(
                        lerp(
                            MaterialTheme.colorScheme.background,
                            Color.White,
                            0.75f
                        ),
                        Color.Black
                    )
                } else null,
                modifier = Modifier
                    .constrainAs(illustration) {
                        start.linkTo(parent.start, 16.dp)
                        bottom.linkTo(parent.bottom, 16.dp)
                        end.linkTo(illustrationBarrier, 8.dp)
                        top.linkTo(parent.top, 16.dp)
                        width = fillToConstraints
                        height = fillToConstraints
                    }
            )
        }
    }
}

@Composable
private fun CountdownUnit(
    @PluralsRes unitLabel: Int, value: Int, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(vertical = 8.dp)
    ) {
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
            },
        ) { count ->
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
            )
        }
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = pluralStringResource(unitLabel, value),
                textAlign = TextAlign.Center,
            )
            // Always keep one invisible plural string to prevent "jumping" of text
            Text(
                text = pluralStringResource(unitLabel, 10),
                textAlign = TextAlign.Center,
                modifier = Modifier.invisible()
            )
        }
    }
}


@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(fontScale = 1.6f)
@Preview(fontScale = 1.8f)
@Preview(fontScale = 0.6f)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCountdownCard() {
    StoppelMapTheme(themeSetting = ThemeSetting.System) {
        CountdownCard(
            days = 129,
            hours = 1,
            minutes = 20,
            seconds = 12,
            season = object : Season {
                override val year = 2024
                override val iteration = 724
                override val days: List<LocalDate> = emptyList()
                override val start: LocalDateTime = LocalDateTime(2024, Month.AUGUST, 15, 18, 30)
                override val end: LocalDateTime = LocalDateTime(2024, Month.AUGUST, 20, 23, 0)
            }
        )
    }
}