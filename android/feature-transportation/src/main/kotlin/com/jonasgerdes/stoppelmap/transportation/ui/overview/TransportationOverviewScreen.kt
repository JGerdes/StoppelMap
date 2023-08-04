@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.jonasgerdes.stoppelmap.transportation.ui.overview

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.DirectionsTransit
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.theme.components.LoadingSpinner
import com.jonasgerdes.stoppelmap.theme.modifier.elevationWhenScrolled
import com.jonasgerdes.stoppelmap.theme.spacing.defaultContentPadding
import com.jonasgerdes.stoppelmap.transportation.R
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import com.jonasgerdes.stoppelmap.transportation.ui.overview.TransportationOverviewViewModel.FavouriteState
import com.jonasgerdes.stoppelmap.transportation.ui.route.StopStationCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("NewApi")
@Composable
fun TransportationOverviewScreen(
    onRouteTap: (routeId: String) -> Unit,
    onStationTap: (stationId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransportationOverviewViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.transportation_overview_topbar_title)) },
                scrollBehavior = scrollBehavior,
                modifier = Modifier.elevationWhenScrolled(listState)
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        val favouriteState = state.favouriteState
        val trainRoutesState = state.trainRoutesState
        val busRoutesState = state.busRoutesViewState

        if (favouriteState is FavouriteState.Loaded) {
            LazyColumn(
                contentPadding = defaultContentPadding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                if (favouriteState.favouriteStations.isNotEmpty()) {
                    stickyHeader(key = "key_your_stations") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(8.dp),
                        ) {
                            Icon(Icons.Rounded.Star, contentDescription = null)
                            Text(
                                text = stringResource(R.string.transportation_overview_section_favourite),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    items(
                        items = favouriteState.favouriteStations,
                        key = { it.id },
                        contentType = { ItemTypes.FavouriteStation }) { station ->
                        StopStationCard(
                            station = station,
                            highlight = true,
                            modifier = Modifier.clickable {
                                onStationTap(station.id)
                            }
                        )
                    }
                }
                stickyHeader {
                    SectionHeader(
                        Icons.Rounded.DirectionsTransit,
                        stringResource(R.string.transportation_overview_section_train)
                    )
                }
                items(
                    items = trainRoutesState.routes,
                    key = { it.id },
                    contentType = { ItemTypes.Route }) { route ->
                    RouteSummaryCard(onRouteTap, route)
                }
                
                stickyHeader {
                    SectionHeader(
                        Icons.Rounded.DirectionsBus,
                        stringResource(R.string.transportation_overview_section_bus)
                    )
                }
                items(
                    items = busRoutesState.routes,
                    key = { it.id },
                    contentType = { ItemTypes.Route }) { route ->
                    RouteSummaryCard(onRouteTap, route)
                }
            }
        } else {
            LoadingSpinner(Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
    ) {
        Icon(icon, contentDescription = null)
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun RouteSummaryCard(
    onRouteTap: (routeId: String) -> Unit,
    route: RouteSummary
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onRouteTap(route.id)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = route.title,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.transportation_overview_card_via),
                style = MaterialTheme.typography.labelSmall
            )
            route.viaStations.forEach { station ->
                Text(
                    text = station,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}


enum class ItemTypes {
    FavouriteStation,
    Route,
}
