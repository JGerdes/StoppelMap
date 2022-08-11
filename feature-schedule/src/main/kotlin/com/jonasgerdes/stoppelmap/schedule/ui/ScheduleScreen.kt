@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLifecycleComposeApi::class, ExperimentalLifecycleComposeApi::class,
    ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPagerApi::class,
)

package com.jonasgerdes.stoppelmap.schedule.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jonasgerdes.stoppelmap.schedule.R
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.androidx.compose.viewModel
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    lazyViewModel: Lazy<ScheduleViewModel> = viewModel()
) {
    val viewModel by lazyViewModel
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.schedule_topbar_title)) }
        )
        val topShape = remember {
            RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
        }
        Card(
            shape = topShape,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Card(
                shape = topShape,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                Column(modifier.fillMaxWidth()) {
                    val pagerState = rememberPagerState()
                    val scheduleDays = state.scheduleState.scheduleDays
                    val scope = rememberCoroutineScope()

                    state.scheduleState.selectedDay?.let { index ->
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                    Card(
                        shape = topShape,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.fillMaxWidth()) {
                            scheduleDays.forEachIndexed { index, day ->
                                Text(
                                    text = stringResource(day.date.dayOfWeek.toResourceString()),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable {
                                            viewModel.onDayTap(day)
                                        }
                                        .then(
                                            if (index == pagerState.currentPage) {
                                                Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                                            } else Modifier
                                        )
                                        .padding(vertical = 12.dp)
                                )
                            }
                        }
                    }
                    HorizontalPager(
                        count = scheduleDays.size,
                        state = pagerState,
                    ) { page ->
                        val scheduleDay = scheduleDays[page]
                        val formatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
                        LazyColumn(
                            Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            items(
                                items = scheduleDay.events,
                                key = { it.slug }
                            ) { event ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                    ) {
                                        Text(
                                            text = event.name,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Rounded.Schedule,
                                                contentDescription = stringResource(R.string.schedule_startTime_contentDescription)
                                            )
                                            Spacer(modifier = Modifier.size(8.dp))
                                            Text(
                                                text = event.start.toJavaLocalDateTime()
                                                    .format(formatter)
                                            )
                                        }
                                        if (event.location != null) {
                                            Spacer(modifier = Modifier.size(4.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Rounded.LocationOn,
                                                    contentDescription = stringResource(R.string.schedule_location_contentDescription)
                                                )
                                                Spacer(modifier = Modifier.size(8.dp))
                                                Text(text = event.location!!)
                                            }
                                        }
                                        if (event.description != null) {
                                            Spacer(modifier = Modifier.size(4.dp))
                                            Text(
                                                text = event.description!!,
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.padding(
                                                    start = 32.dp,
                                                )
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Divider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun DayOfWeek.toResourceString() = when (this) {
    DayOfWeek.MONDAY -> R.string.schedule_day_monday
    DayOfWeek.TUESDAY -> R.string.schedule_day_tuesday
    DayOfWeek.WEDNESDAY -> R.string.schedule_day_wednesday
    DayOfWeek.THURSDAY -> R.string.schedule_day_thursday
    DayOfWeek.FRIDAY -> R.string.schedule_day_friday
    DayOfWeek.SATURDAY -> R.string.schedule_day_saturday
    DayOfWeek.SUNDAY -> R.string.schedule_day_sunday
}
