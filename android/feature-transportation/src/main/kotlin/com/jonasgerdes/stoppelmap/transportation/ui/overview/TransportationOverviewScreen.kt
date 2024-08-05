@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.jonasgerdes.stoppelmap.transportation.ui.overview

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.DirectionsTransit
import androidx.compose.material.icons.rounded.LocalTaxi
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.theme.components.FancyAnimatedIndicator
import com.jonasgerdes.stoppelmap.transportation.R
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("NewApi")
@Composable
fun TransportationOverviewScreen(
    onRouteTap: (routeId: String) -> Unit,
    onStationTap: (stationId: String) -> Unit,
    onPhoneNumberTap: (phoneNumber: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransportationOverviewViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val pages = remember {
        listOf(
            Page.Bus,
            Page.Train,
            Page.Taxi,
        )
    }

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(id = R.string.transportation_overview_topbar_title)) },
                )
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions: List<TabPosition> ->
                        FancyAnimatedIndicator(
                            tabPositions = tabPositions,
                            selectedTabIndex = pagerState.currentPage,
                        )
                    },
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    pages.forEachIndexed { index, page ->
                        val color by animateColorAsState(
                            targetValue =
                            if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onBackground,
                        )
                        Tab(
                            selected = index == pagerState.currentPage,
                            icon = {
                                Icon(page.icon, null, tint = color)
                            },
                            text = {
                                Text(
                                    text = stringResource(page.text),
                                    color = color,
                                )
                            },
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(index)
                                }
                            }
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
        ) { pageIndex ->
            when (pages[pageIndex]) {
                Page.Bus -> BusPage(
                    state = state.busRoutesViewState,
                    onStationTap = onStationTap,
                    onRouteTap = onRouteTap,
                )

                Page.Train -> TrainPage(state = state.trainRoutesState, onRouteTap = onRouteTap)
                Page.Taxi -> TaxiPage(
                    state = state.taxiServicesState,
                    onPhoneNumberTap = onPhoneNumberTap
                )
            }
        }

    }
}


sealed class Page(
    val icon: ImageVector,
    @StringRes val text: Int
) {
    object Bus : Page(
        Icons.Rounded.DirectionsBus,
        R.string.transportation_overview_section_bus,
    )

    object Train : Page(
        Icons.Rounded.DirectionsTransit,
        R.string.transportation_overview_section_train
    )

    object Taxi : Page(
        Icons.Rounded.LocalTaxi,
        R.string.transportation_overview_section_taxi,
    )
}

@Composable
fun BusPage(
    state: TransportationOverviewViewModel.BusRoutesState,
    onStationTap: (stationId: String) -> Unit,
    onRouteTap: (routeId: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        /*items(
            items = state.favouriteStations,
            key = { it.slug },
            contentType = { ItemTypes.FavouriteStation }) { station ->
            StopStationCard(
                station = station,
                highlight = true,
                modifier = Modifier.clickable {
                    onStationTap(station.slug)
                }
            )
        }*/
        items(
            items = state.routes,
            key = { it.slug },
            contentType = { ItemTypes.Route }) { route ->
            RouteRow(route = route, onRouteTap = onRouteTap)
        }
    }
}

@Composable
fun TrainPage(
    state: TransportationOverviewViewModel.TrainRoutesState,
    onRouteTap: (routeSlug: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = state.routes,
            key = { it.slug },
            contentType = { ItemTypes.Route }) { route ->
            RouteRow(route = route, onRouteTap = onRouteTap)
        }
    }
}

@Composable
private fun RouteRow(
    route: RouteSummary,
    onRouteTap: (routeSlug: String) -> Unit
) {
    ListItem(
        headlineContent = { Text(route.name) },
        trailingContent = {
            Icon(Icons.Rounded.ChevronRight, null)
        },
        modifier = Modifier.clickable { onRouteTap(route.slug) }
    )
}

@Composable
fun TaxiPage(
    state: TransportationOverviewViewModel.TaxiServicesState,
    onPhoneNumberTap: (phoneNumber: String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = state.services,
            key = { it.title },
            contentType = { ItemTypes.Route }) { service ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onPhoneNumberTap(service.phoneNumber) }
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = service.title,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = service.phoneNumberFormatted
                                ?: service.phoneNumber,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    IconButton(onClick = { onPhoneNumberTap(service.phoneNumber) }) {
                        Icon(
                            Icons.Rounded.Call,
                            contentDescription = stringResource(R.string.transportation_overview_card_action_call_contentDescription)
                        )
                    }
                }
            }
        }
    }
}

enum class ItemTypes {
    FavouriteStation,
    Route,
}
