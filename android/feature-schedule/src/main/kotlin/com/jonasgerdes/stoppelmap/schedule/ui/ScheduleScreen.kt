package com.jonasgerdes.stoppelmap.schedule.ui

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.resources.toFullResourceString
import com.jonasgerdes.stoppelmap.schedule.R
import com.jonasgerdes.stoppelmap.schedule.model.BookmarkedEvents
import com.jonasgerdes.stoppelmap.schedule.ui.components.EventCard
import com.jonasgerdes.stoppelmap.theme.components.EventRow
import com.jonasgerdes.stoppelmap.theme.components.ListLineHeader
import com.jonasgerdes.stoppelmap.theme.material.appBarContainerColor
import com.jonasgerdes.stoppelmap.theme.util.MeasureUnconstrainedViewWidth
import com.jonasgerdes.stoppelmap.theme.util.localizedString
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalTime
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
)
@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()


    val sheetState = rememberStandardBottomSheetState(
        initialValue = if (state.bookmarkedEvents is BookmarkedEvents.Some) SheetValue.PartiallyExpanded else SheetValue.Hidden,
        confirmValueChange = { it != SheetValue.Hidden || state.bookmarkedEvents is BookmarkedEvents.None },
        skipHiddenState = state.bookmarkedEvents is BookmarkedEvents.Some
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val hasAnyBookmarkedEvents by remember {
        derivedStateOf { state.bookmarkedEvents is BookmarkedEvents.Some }
    }

    LaunchedEffect(hasAnyBookmarkedEvents) {
        if (hasAnyBookmarkedEvents) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    val pendingBookmarkRemoval = remember {
        mutableStateOf<String?>(null)
    }
    val peekHeight = BottomSheetDefaults.SheetPeekHeight + 96.dp
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Box(modifier = Modifier.fillMaxSize()) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = peekHeight,
            sheetContent = {
                (state.bookmarkedEvents as? BookmarkedEvents.Some)?.let {
                    SheetContent(
                        it,
                        onBookmarkToggle = { slug, isBookmarked ->
                            if (isBookmarked) viewModel.onEventNotificationSchedule(slug, true)
                            else pendingBookmarkRemoval.value = slug
                        }
                    )
                }
            },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(id = R.string.schedule_topbar_title)) },
                    scrollBehavior = scrollBehavior,
                )
            },
            modifier = modifier,
        ) { paddingValues ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                val scheduleDays = state.scheduleDays
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    pageCount = { scheduleDays.size }
                )
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
                val containerColor by appBarContainerColor(scrollBehavior)
                PrimaryTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = containerColor,
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
                    state = pagerState,
                ) { page ->
                    val scheduleDay = scheduleDays[page]
                    val formatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
                    val bottomPadding by remember(state.bookmarkedEvents) {
                        derivedStateOf {
                            8.dp + if (state.bookmarkedEvents is BookmarkedEvents.Some) peekHeight else 0.dp
                        }
                    }
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 8.dp,
                            top = 8.dp,
                            end = 8.dp,
                            bottom = bottomPadding
                        ),
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
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
                                        name = localizedString(event.name),
                                        description = event.description?.let { localizedString(it) },
                                        locationName = event.locationName,
                                        isBookmarked = event.isBookmarked,
                                        selected = event == state.selectedEvent,
                                        onSelected = { viewModel.onEventTap(event) },
                                        onNotificationToggle = {
                                            viewModel.onEventNotificationSchedule(
                                                event.slug,
                                                notificationActive = it
                                            )
                                        },
                                        showNotificationToggle = true,
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        pendingBookmarkRemoval.value?.let { pendingBookmarkRemovalSlug ->
            AlertDialog(
                title = { Text(stringResource(R.string.schedule_bookmarked_unsave_dialog_title)) },
                onDismissRequest = {
                    pendingBookmarkRemoval.value = null
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.onEventNotificationSchedule(pendingBookmarkRemovalSlug, false)
                        pendingBookmarkRemoval.value = null
                    }) {
                        Text(stringResource(R.string.schedule_bookmarked_unsave_dialog_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        pendingBookmarkRemoval.value = null
                    }) {
                        Text(stringResource(R.string.schedule_bookmarked_unsave_dialog_dismiss))
                    }
                }
            )
        }
    }
}

@Composable
fun SheetContent(
    bookmarkedEvents: BookmarkedEvents.Some,
    onBookmarkToggle: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            stringResource(R.string.schedule_bookmarked_list_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = spacedBy(8.dp)) {
            items(bookmarkedEvents.upcoming, key = { it.slug }) { event ->
                EventCard(
                    event = event,
                    modifier = Modifier.fillMaxWidth(),
                    onBookmarkToggle = { onBookmarkToggle(event.slug, it) }
                )
            }
            if (bookmarkedEvents.past.isNotEmpty()) {
                item {
                    ListLineHeader(Modifier.padding(top = 16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.schedule_bookmarked_list_past))
                        }
                    }
                }
                items(bookmarkedEvents.past, key = { it.slug }) { event ->
                    EventCard(
                        event = event,
                        modifier = Modifier.fillMaxWidth(),
                        onBookmarkToggle = { onBookmarkToggle(event.slug, it) }
                    )
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
