@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.jonasgerdes.stoppelmap.transportation.ui.route

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.theme.components.ListLineHeader
import com.jonasgerdes.stoppelmap.theme.components.LoadingSpinner
import com.jonasgerdes.stoppelmap.theme.modifier.elevationWhenScrolled
import com.jonasgerdes.stoppelmap.theme.spacing.defaultContentPadding
import com.jonasgerdes.stoppelmap.theme.util.stringDesc
import com.jonasgerdes.stoppelmap.transportation.R
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails
import com.jonasgerdes.stoppelmap.transportation.ui.getFormattedStringRes
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import kotlin.math.roundToInt

@SuppressLint("NewApi")
@Composable
fun RouteScreen(
    routeId: String,
    onStationTap: (stationId: String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RouteViewModel = koinViewModel { parametersOf(routeId) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Timber.d("new state: ${(state.routeState as? RouteViewModel.RouteState.Loaded)?.routeDetails?.stations?.firstOrNull()}")
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

    val routeState = state.routeState
    if (routeState is RouteViewModel.RouteState.Loaded) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = routeState.routeDetails.title) },
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier.elevationWhenScrolled(listState),
                    navigationIcon = {
                        IconButton(
                            onClick = { onNavigateBack() }
                        ) {
                            Icon(
                                Icons.Rounded.ArrowBack,
                                stringResource(id = R.string.transportation_route_topbar_navigateBack_contentDescription)
                            )
                        }
                    })
            },
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { paddingValues ->
            val stations = routeState.routeDetails.stations
            LazyColumn(
                contentPadding = defaultContentPadding(paddingValues),
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                routeState.routeDetails.additionalInfo?.let { additionalInfo ->
                    item {
                        OutlinedCard(
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = additionalInfo,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
                item(key = "section_there", contentType = ItemTypes.SectionTitle) {
                    ListLineHeader(Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.transportation_route_section_there),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                items(
                    count = stations.size,
                    key = { stations[it].id },
                    contentType = {
                        when (stations[it]) {
                            is BusRouteDetails.Station.Stop -> ItemTypes.Station
                            is BusRouteDetails.Station.Destination -> ItemTypes.DestinationStation
                        }
                    }
                ) { index ->
                    val station = stations[index]
                    val isFirst = index == 0
                    val isLast = index == stations.lastIndex
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(16.dp)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            val color = MaterialTheme.colorScheme.primary
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .padding(
                                        top = if (isFirst) 32.dp else 0.dp,
                                    )
                                    .fillMaxHeight(fraction = if (isLast) 0.5f else 1f)
                                    .background(color)
                                    .align(
                                        when {
                                            isFirst -> Alignment.BottomCenter
                                            isLast -> Alignment.TopCenter
                                            else -> Alignment.Center
                                        }
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .padding(top = 32.dp)
                                    .size(8.dp)
                                    .background(color, CircleShape)
                            )

                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        when (station) {
                            is BusRouteDetails.Station.Stop -> {
                                StopStationCard(
                                    station = station,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 8.dp)
                                        .clickable {
                                            onStationTap(station.id)
                                        }
                                )
                            }

                            is BusRouteDetails.Station.Destination -> {
                                DestinationStationCard(
                                    station = station,
                                    Modifier
                                        .weight(1f)
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
                item(key = "section_back", contentType = ItemTypes.SectionTitle) {
                    ListLineHeader(Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.transportation_route_section_back),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                val returnStations = routeState.routeDetails.returnStations
                items(
                    count = returnStations.size,
                    key = { returnStations[it].id },
                    contentType = { ItemTypes.ReturnStation }
                ) {
                    val station = returnStations[it]
                    ReturnStationCard(
                        station = station,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                onStationTap(station.id)
                            }
                    )
                }
            }
        }
    } else {
        LoadingSpinner(modifier.fillMaxSize())
    }
}

@Composable
fun StopStationCard(
    station: BusRouteDetails.Station.Stop,
    highlight: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        border = if (highlight) BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary) else null,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                station.routeName?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(end = 64.dp, bottom = 8.dp)
                    )
                }
                Text(
                    text = station.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(end = 64.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.transportation_route_card_next_departures_label),
                    style = MaterialTheme.typography.labelMedium
                )
                if (station.nextDepartures.isNotEmpty()) {
                    station.nextDepartures.forEach { departureTime ->
                        Text(text = stringDesc(departureTime.getFormattedStringRes()))
                    }
                } else {
                    Text(
                        text = stringResource(
                            R.string.transportation_route_card_next_departures_none
                        )
                    )
                }
            }
            if (station.annotateAsNew) {
                CornerRibbon(text = stringResource(R.string.transportation_route_card_annotation_new))
            }
        }
    }
}

@Composable
private fun CornerRibbon(
    text: String,
    modifier: Modifier = Modifier
) {
    Layout(
        measurePolicy = remember { CornerRibbonMeasurePolicy() },
        content = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .rotate(45f)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(vertical = 2.dp, horizontal = 32.dp)
            )
        },
        modifier = modifier.fillMaxWidth()
    )
}

private class CornerRibbonMeasurePolicy : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        val placables = measurables.map { it.measure(Constraints()) }
        return layout(constraints.maxWidth, constraints.minHeight) {
            placables.forEach {
                it.placeRelative(
                    ((constraints.maxWidth - it.width / 2) - it.width / 5f).roundToInt(),
                    ((it.height / -2) + it.width / 5f).roundToInt()
                )
            }
        }
    }
}

@Composable
fun DestinationStationCard(
    station: BusRouteDetails.Station.Destination,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = station.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun ReturnStationCard(
    station: BusRouteDetails.ReturnStation,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = stringResource(
                    R.string.transportation_route_return_station_from,
                    station.title
                ),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(end = 64.dp)
            )
        }
    }
}

enum class ItemTypes {
    Station,
    SectionTitle,
    DestinationStation,
    ReturnStation,
}
