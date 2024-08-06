@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.jonasgerdes.stoppelmap.transportation.ui.station

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.theme.components.ListLineHeader
import com.jonasgerdes.stoppelmap.theme.components.LoadingSpinner
import com.jonasgerdes.stoppelmap.theme.modifier.elevationWhenScrolled
import com.jonasgerdes.stoppelmap.transportation.R
import com.jonasgerdes.stoppelmap.transportation.model.StationDetails
import com.jonasgerdes.stoppelmap.transportation.model.Timetable
import com.jonasgerdes.stoppelmap.transportation.ui.toStringResource
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale

@SuppressLint("NewApi")
@Composable
fun StationScreen(
    stationId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StationViewModel = koinViewModel { parametersOf(stationId) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()


    val stationState = state.stationState
    if (stationState is StationViewModel.StationState.Loaded) {
        Column(
            modifier = modifier
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = stationState.stationName,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
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
                    IconButton(onClick = viewModel::toggleFavourite) {
                        Icon(
                            if (stationState.isFavourite) Icons.Rounded.Star
                            else Icons.Rounded.StarOutline,
                            if (stationState.isFavourite) stringResource(R.string.transportation_station_topbar_action_unfavourite_contentDescription)
                            else stringResource(R.string.transportation_station_topbar_action_favourite_contentDescription)
                        )
                    }
                },
            )
            val priceState = stationState.priceState
            if (priceState.prices.isNotEmpty()) {
                OutlinedCard(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
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
                        if (priceState.showDeutschlandTicketHint) {
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = stringResource(R.string.transportation_station_prices_hint_deutschland_ticket),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
            }
            val topShape = remember {
                RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            }
            Card(
                shape = topShape,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                Column(Modifier.fillMaxWidth()) {
                    val gridState = rememberLazyGridState()
                    Card(
                        shape = topShape,
                        modifier = Modifier.elevationWhenScrolled(gridState)
                    ) {
                        Row {
                            stationState.timetable.departureDays.forEach {
                                Text(
                                    text = stringResource(it.dayOfWeek.toStringResource().resourceId),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .padding(vertical = 16.dp)
                                        .weight(1f)
                                )
                            }
                        }
                    }
                    Timetable(
                        timetable = stationState.timetable,
                        gridState = gridState,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    } else {
        LoadingSpinner(Modifier.fillMaxSize())
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
fun Timetable(
    timetable: Timetable,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
) {
    val timeFormatter = remember(Locale.getDefault()) {
        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    }
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
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
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
