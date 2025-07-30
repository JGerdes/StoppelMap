@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.jonasgerdes.stoppelmap.transportation.ui.route

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ConfirmationNumber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.jonasgerdes.stoppelmap.theme.components.LoadingSpinner
import com.jonasgerdes.stoppelmap.theme.spacing.defaultContentPadding
import com.jonasgerdes.stoppelmap.theme.util.stringDesc
import com.jonasgerdes.stoppelmap.transportation.R
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails.Station.NextDepartures
import com.jonasgerdes.stoppelmap.transportation.ui.getFormattedStringRes
import com.jonasgerdes.stoppelmap.transportation.ui.route.RouteViewModel.RouteState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

@SuppressLint("NewApi")
@Composable
fun RouteScreen(
    routeId: String,
    routeName: String?,
    onStationTap: (stationId: String, stationName: String) -> Unit,
    onWebsiteTap: (url: String) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RouteViewModel = koinViewModel { parametersOf(routeId) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()

    val routeState = state.routeState
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    ((routeState as? RouteState.Loaded)?.routeDetails?.name ?: routeName)?.let {
                        Text(text = it)
                    }
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigateUp() }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            stringResource(id = R.string.transportation_route_topbar_navigateBack_contentDescription)
                        )
                    }
                })
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        if (routeState is RouteState.Loaded) {
            val stations = routeState.routeDetails.stations
            LazyColumn(
                contentPadding = defaultContentPadding(paddingValues, bottom = 8.dp),
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                with(routeState.routeDetails) {
                    if (additionalInfo != null || ticketWebsites.isNotEmpty())
                        item {
                            OutlinedCard(
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .fillMaxWidth()
                            ) {
                                Column(
                                    verticalArrangement = spacedBy(8.dp),
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                ) {
                                    additionalInfo?.let {
                                        Text(text = it)
                                    }
                                    ticketWebsites.forEach {
                                        Button(
                                            onClick = { onWebsiteTap(it.url) },
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            Row(horizontalArrangement = spacedBy(8.dp)) {
                                                Icon(Icons.Rounded.ConfirmationNumber, null)
                                                Text(
                                                    it.label
                                                        ?: stringResource(id = R.string.transportation_route_ticket_website_label)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                }
                items(
                    count = stations.size,
                    key = { stations[it].slug },
                    contentType = { ItemTypes.Station }
                ) { index ->
                    val station = stations[index]
                    StationRow(isFirst = index == 0, isLast = false) {
                        StopStationCard(
                            station = station,
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp)
                                .clickable {
                                    onStationTap(station.slug, station.name)
                                }
                        )
                    }
                }

                item {
                    StationRow(isFirst = false, isLast = true) {
                        DestinationStationCard(
                            destination = routeState.routeDetails.destination,
                            Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp)
                        )
                    }
                }

                item {
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.transportation_route_operated_by,
                                routeState.routeDetails.operator.name
                            ),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        } else {
            LoadingSpinner(modifier.fillMaxSize())
        }
    }
}

@Composable
private fun StationRow(
    isFirst: Boolean,
    isLast: Boolean,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                // 24.dp + spacer below + defaultContentPadding.start = 56.dp = start of topbar title
                .width(24.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            val color = MaterialTheme.colorScheme.primary
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .padding(
                        top = if (isFirst) 32.dp else 0.dp,
                        bottom = if (isLast) 32.dp else 0.dp,
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
                    .padding(top = 28.dp)
                    .size(16.dp)
                    .background(color, CircleShape)
            )

        }
        Spacer(
            modifier = Modifier
                .size(16.dp)
        )
        content()
    }
}

@Composable
fun StopStationCard(
    station: BusRouteDetails.Station,
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
                Text(
                    text = station.name,
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
                when (val nextDepartures = station.nextDepartures) {
                    is NextDepartures.Loaded -> {
                        if (nextDepartures.departures.isNotEmpty()) {
                            nextDepartures.departures.forEach { departureTime ->
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

                    NextDepartures.Loading -> {
                        listOf("Loading", "the", "times").forEach {
                            Text(
                                it,
                                Modifier.background(LocalContentColor.current)
                            )
                        }
                    }
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
    destination: BusRouteDetails.Destination,
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
                text = "Stoppelmarkt",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            destination.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

enum class ItemTypes {
    Station,
    Destination,
}
