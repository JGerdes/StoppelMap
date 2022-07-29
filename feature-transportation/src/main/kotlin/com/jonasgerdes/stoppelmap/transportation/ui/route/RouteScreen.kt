@file:OptIn(
    ExperimentalLifecycleComposeApi::class,
    ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class,
)

package com.jonasgerdes.stoppelmap.transportation.ui.route

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.theme.components.LoadingSpinner
import com.jonasgerdes.stoppelmap.transportation.R
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf

@SuppressLint("NewApi")
@Composable
fun RouteScreen(
    routeId: String,
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
                title = { Text(text = routeState.route.title) },
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
            val stations = routeState.route.stations
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = stations.size,
                    key = { stations[it].id },
                    contentType = {
                        if (stations[it].isDestination) ItemTypes.DestinationStation
                        else ItemTypes.Station
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
                            contentAlignment = Alignment.Center
                        ) {
                            val color = MaterialTheme.colorScheme.primary
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .fillMaxHeight(fraction = if (isFirst || isLast) 0.5f else 1f)
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
                                    .size(8.dp)
                                    .background(color, CircleShape)
                            )

                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Card(
                            colors = if (station.isDestination) {
                                CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            } else CardDefaults.cardColors(),
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = station.title,
                                    style = MaterialTheme.typography.titleMedium
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


enum class ItemTypes {
    Station,
    DestinationStation
}
