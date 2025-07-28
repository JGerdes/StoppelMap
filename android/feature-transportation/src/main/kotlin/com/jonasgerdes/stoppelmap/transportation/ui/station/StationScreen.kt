@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.jonasgerdes.stoppelmap.transportation.ui.station

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Attractions
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onLayoutRectChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.theme.components.ListLineHeader
import com.jonasgerdes.stoppelmap.theme.components.LoadingSpinner
import com.jonasgerdes.stoppelmap.transportation.R
import com.jonasgerdes.stoppelmap.transportation.model.StationDetails
import com.jonasgerdes.stoppelmap.transportation.model.Timetable
import com.jonasgerdes.stoppelmap.transportation.ui.station.StationViewModel.StationState
import com.jonasgerdes.stoppelmap.transportation.ui.toStringResource
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import kotlin.math.abs

@SuppressLint("NewApi")
@Composable
fun StationScreen(
    stationId: String,
    stationName: String?,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StationViewModel = koinViewModel { parametersOf(stationId) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val stationState = state.stationState
    var availableHeight by remember {
        mutableStateOf(0.dp)
    }
    var topBarBottomY by remember {
        mutableIntStateOf(0)
    }
    var dayRowTopY by remember {
        mutableIntStateOf(100)
    }
    val dayRowReachedTop by remember(topBarBottomY, dayRowTopY) {
        derivedStateOf {
            abs(topBarBottomY - dayRowTopY) < 24
        }
    }
    val dynamicContainerColor by animateColorAsState(
        if (dayRowReachedTop) TopAppBarDefaults.topAppBarColors().scrolledContainerColor
        else TopAppBarDefaults.topAppBarColors().containerColor,
    )

    val density = LocalDensity.current
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    ((stationState as? StationState.Loaded)?.stationName ?: stationName)?.let {
                        Text(
                            text = it,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigateBack() }
                    ) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            stringResource(id = R.string.transportation_station_topbar_navigateBack_contentDescription)
                        )
                    }
                },
                actions = {
                    if (stationState is StationState.Loaded) {
                        IconButton(onClick = viewModel::toggleFavourite) {
                            Icon(
                                if (stationState.isFavourite) Icons.Rounded.Star
                                else Icons.Rounded.StarOutline,
                                if (stationState.isFavourite) stringResource(R.string.transportation_station_topbar_action_unfavourite_contentDescription)
                                else stringResource(R.string.transportation_station_topbar_action_favourite_contentDescription)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = dynamicContainerColor,
                ),
                modifier = Modifier
                    .onLayoutRectChanged(throttleMillis = 100) {
                        topBarBottomY = it.boundsInRoot.bottom
                    }
            )
        }
    ) { paddingValues ->
        if (stationState is StationState.Loaded) {
            Column(
                Modifier
                    .padding(paddingValues)
                    .fillMaxHeight()
                    .onGloballyPositioned {
                        availableHeight = with(density) { it.size.height.toDp() }
                    }
                    .verticalScroll(state = rememberScrollState())
            ) {
                val priceState = stationState.priceState
                if (priceState.prices.isNotEmpty()) {
                    OutlinedCard(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.transportation_station_prices_title),
                                style = MaterialTheme.typography.labelMedium
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            priceState.prices.forEach { fee ->
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = fee.name
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(
                                        text = fee.formatAmount(LocalContext.current.resources)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            Text(
                                text = stringResource(R.string.transportation_station_prices_hint_cash),
                                style = MaterialTheme.typography.labelMedium
                            )
                            if (priceState.showDeutschlandTicketHint) {
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text = stringResource(R.string.transportation_station_prices_hint_deutschland_ticket),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }

                        }
                    }
                }
                stationState.additionalInfo?.let { info ->
                    OutlinedCard(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = info,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val pages = listOfNotNull(stationState.outwardTimetable, stationState.returnTimetable)
                val pagerState = rememberPagerState(pageCount = { pages.size })
                val coroutineScope = rememberCoroutineScope()

                SecondaryTabRow(
                    selectedTabIndex = pagerState.currentPage,
                ) {
                    Tab(
                        selected = pagerState.currentPage == 0,
                        icon = { Icon(Icons.Rounded.Attractions, null) },
                        text = {
                            Text(stringResource(R.string.transportation_station_timetable_outward))
                        },
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                    )
                    if (stationState.returnTimetable != null) {
                        Tab(
                            selected = pagerState.currentPage == 1,
                            icon = { Icon(Icons.Rounded.Home, null) },
                            text = {
                                Text(stringResource(R.string.transportation_station_timetable_return))
                            },
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                            },
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .height(availableHeight)
                        .fillMaxWidth()
                ) { pageIndex ->
                    val timetable = pages[pageIndex]
                    Column {
                        DayHeaderRow(
                            departureDays = timetable.departureDays,
                            containerColor = dynamicContainerColor,
                            modifier = Modifier
                                .onLayoutRectChanged(throttleMillis = 100) {
                                    dayRowTopY = it.boundsInRoot.top
                                }
                        )
                        Timetable(
                            timetable = timetable,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
            }

        } else {
            LoadingSpinner(Modifier.fillMaxSize())
        }
    }
}

private fun StationDetails.Fee.formatAmount(resources: Resources) =
    NumberFormat.getCurrencyInstance(
        ConfigurationCompat.getLocales(resources.configuration)[0] ?: Locale.getDefault()
    ).apply {
        maximumFractionDigits = 2
        currency = Currency.getInstance("EUR")
    }.format(price / 100f)


private val timeFormat = LocalTime.Format {
    hour()
    char(':')
    minute()
}

@Composable
fun DayHeaderRow(
    departureDays: List<Timetable.DepartureDay>,
    containerColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .background(containerColor)
            .padding(vertical = 8.dp)
    ) {
        departureDays.forEach {
            val backgroundModifier =
                if (it.isToday) Modifier
                    .padding(horizontal = 4.dp)
                    .background(
                        NavigationBarItemDefaults.colors().selectedIndicatorColor,
                        CircleShape
                    )
                else Modifier.padding(horizontal = 4.dp)
            Text(
                text = stringResource(it.date.dayOfWeek.toStringResource().resourceId),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = backgroundModifier
                    .padding(vertical = 8.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
fun Timetable(
    timetable: Timetable,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(timetable.departureDays.size),
        contentPadding = PaddingValues(bottom = 16.dp),
        state = gridState,
        modifier = modifier
    ) {
        timetable.daySegments.forEach { daySegment ->
            item(
                key = daySegment.type,
                contentType = ContentType.HEADER,
                span = { GridItemSpan(maxLineSpan) }) {
                ListLineHeader(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(daySegment.type.toStringResource().resourceId),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            items(
                daySegment.departureSlots.flatMap { dslot -> dslot.departures.mapIndexed() { id, it -> it to id.toString() + "-" + dslot.departures.first { it != null }!!.time } },
                key = { it.second },
                contentType = { ContentType.TIME }
            ) { slotPair ->
                val slot = slotPair.first
                if (slot != null) {
                    Text(
                        text = slot.time.time.format(timeFormat),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textDecoration = if (slot.isInPast) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .alpha(if (slot.isInPast) 0.4f else 1f)
                            .fillMaxWidth()
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}

enum class ContentType {
    HEADER, TIME
}