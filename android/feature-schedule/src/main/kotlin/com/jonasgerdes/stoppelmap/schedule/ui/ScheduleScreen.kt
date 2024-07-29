package com.jonasgerdes.stoppelmap.schedule.ui

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jonasgerdes.stoppelmap.schedule.R
import com.jonasgerdes.stoppelmap.schedule.ui.components.EventRow
import com.jonasgerdes.stoppelmap.theme.components.FancyAnimatedIndicator
import com.jonasgerdes.stoppelmap.theme.components.NoticeCard
import com.jonasgerdes.stoppelmap.theme.util.MeasureUnconstrainedViewWidth
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalTime
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPagerApi::class, ExperimentalFoundationApi::class,
)
@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.schedule_topbar_title)) },
            )
        },
        bottomBar = {
            NoticeCard(title = "Stand 2023", message = "Ein Update für 2024 ist in Arbeit")
        },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            val pagerState = rememberPagerState()
            val scheduleDays = state.scheduleDays
            val scope = rememberCoroutineScope()

            state.selectedDay?.let { index ->
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }

            SideEffect {
                if (pagerState.isScrollInProgress) {
                    viewModel.onDayTap(null)
                }
            }

            val selectedTabIndex = pagerState.currentPage
            TabRow(
                selectedTabIndex = selectedTabIndex,
                indicator = { tabPositions: List<TabPosition> ->
                    FancyAnimatedIndicator(
                        tabPositions = tabPositions,
                        selectedTabIndex = selectedTabIndex,
                    )
                },
            ) {
                scheduleDays.forEachIndexed { index, day ->
                    val color by animateColorAsState(
                        targetValue =
                        if (selectedTabIndex == index) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground,
                    )
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { viewModel.onDayTap(day.date) },
                        text = {
                            val dayContentDescription =
                                stringResource(day.date.dayOfWeek.toFullResourceString().resourceId)
                            Text(
                                text = stringResource(day.date.dayOfWeek.toShortResourceString().resourceId),
                                fontWeight = FontWeight.Bold,
                                color = color,
                                modifier = Modifier.semantics {
                                    contentDescription = dayContentDescription
                                }
                            )
                        }
                    )
                }
            }
            HorizontalPager(
                count = scheduleDays.size,
                state = pagerState,
            ) { page ->
                val scheduleDay = scheduleDays[page]
                val formatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
                LazyColumn(
                    contentPadding = PaddingValues(8.dp)
                ) {
                    scheduleDay.timeSlots.forEach { timeSlot ->
                        val timeString = formatter.format(timeSlot.time.toJavaLocalTime())
                        items(
                            count = timeSlot.events.size,
                            key = { timeSlot.events[it].slug },
                            contentType = { "event" },
                        ) { id ->
                            val event = timeSlot.events[id]
                            Row {
                                if (id == 0) {
                                    Time(timeString)
                                } else {
                                    MeasureUnconstrainedViewWidth(
                                        viewToMeasure = { Time(timeString) }
                                    ) {
                                        Spacer(modifier = Modifier.width(it))
                                    }
                                }
                                EventRow(
                                    event = event,
                                    selected = event == state.selectedEvent,
                                    onSelected = { viewModel.onEventTap(event) },
                                    onNotificationToggle = {
                                        viewModel.onEventNotificationSchedule(
                                            event,
                                            notificationActive = it
                                        )
                                    },
                                    showNotificationToggle = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Time(timeString: String) {
    Text(
        text = timeString,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(top = 24.dp)
    )
}
