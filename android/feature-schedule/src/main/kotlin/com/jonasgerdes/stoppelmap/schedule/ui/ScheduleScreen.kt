@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPagerApi::class,
)

package com.jonasgerdes.stoppelmap.schedule.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jonasgerdes.stoppelmap.schedule.R
import com.jonasgerdes.stoppelmap.schedule.ui.components.EventRow
import com.jonasgerdes.stoppelmap.theme.spacing.defaultContentPadding
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.schedule_topbar_title)) },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        val topShape = remember {
            RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
        }
        Card(
            shape = topShape,
            modifier = Modifier
                .padding(defaultContentPadding(paddingValues = paddingValues))
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
                                EventRow(
                                    event = event,
                                    timeFormatter = formatter,
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp, horizontal = 16.dp)
                                )
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
