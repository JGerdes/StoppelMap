@file:OptIn(
    ExperimentalLifecycleComposeApi::class,
    ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class,
    ExperimentalLifecycleComposeApi::class,
)

package com.jonasgerdes.stoppelmap.transportation.ui.route

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.theme.components.LoadingSpinner
import com.jonasgerdes.stoppelmap.transportation.R
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("NewApi")
@Composable
fun RouteScreen(
    routeId: String,
    onStationTap: (stationId: String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    lazyViewModel: Lazy<RouteViewModel> = viewModel { parametersOf(routeId) }
) {
    val viewModel by lazyViewModel
    val state by viewModel.state.collectAsStateWithLifecycle()

    val routeState = state.routeState
    if (routeState is RouteViewModel.RouteState.Loaded) {
        Column(
            modifier = modifier
        ) {
            CenterAlignedTopAppBar(
                title = { Text(text = routeState.routeDetails.title) },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigateBack() }
                    ) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            stringResource(id = R.string.transportation_route_topbar_navigateBack_contentDescription)
                        )
                    }
                }
            )
            val stations = routeState.routeDetails.stations
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
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
                                    Modifier
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
                                        .clickable {
                                            onStationTap(station.id)
                                        }
                                )
                            }
                        }
                    }
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
    modifier: Modifier = Modifier
) {
    val departureTimeFormatter = remember(Locale.getDefault()) {
        DateTimeFormatter.ofPattern("dd.MM, HH:mm", Locale.getDefault())
    }

    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = station.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.transportation_route_card_next_departures_label),
                style = MaterialTheme.typography.labelMedium
            )
            station.nextDepartures.forEach { departureTime ->
                val departureText = when (departureTime) {
                    is BusRouteDetails.DepartureTime.Absolut -> departureTimeFormatter.format(
                        departureTime.time.toJavaLocalDateTime()
                    )
                    BusRouteDetails.DepartureTime.Immediately ->
                        stringResource(R.string.transportation_route_card_next_departures_immediately)
                    is BusRouteDetails.DepartureTime.Relative -> TODO()
                }
                Text(
                    text = departureText,
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

enum class ItemTypes {
    Station,
    DestinationStation
}
