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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Attractions
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.data.transportation.DepartureType
import com.jonasgerdes.stoppelmap.theme.components.ListLineHeader
import com.jonasgerdes.stoppelmap.theme.components.LoadingSpinner
import com.jonasgerdes.stoppelmap.theme.modifier.backgroundWhenScrolled
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
import java.util.Currency
import java.util.Locale

@SuppressLint("NewApi")
@Composable
fun StationScreen(
    stationId: String,
    onNavigateBack: () -> Unit,
    padding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: StationViewModel = koinViewModel { parametersOf(stationId) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val stationState = state.stationState
    if (stationState is StationViewModel.StationState.Loaded) {
        val bottomSheetState: SheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            confirmValueChange = { it != SheetValue.Hidden },
            skipHiddenState = true,
        )
        val scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = bottomSheetState,
        )
        var availableHeight by remember {
            mutableStateOf(128.dp)
        }
        var contentHeight by remember {
            mutableStateOf(128.dp)
        }
        val density = LocalDensity.current

        var departureType: DepartureType by remember {
            mutableStateOf(DepartureType.Outward)
        }

        BottomSheetScaffold(
            modifier = modifier.onGloballyPositioned {
                availableHeight = with(density) { it.size.height.toDp() }
            },
            scaffoldState = scaffoldState,
            sheetPeekHeight = androidx.compose.ui.unit.max(
                availableHeight - contentHeight,
                padding.calculateBottomPadding() + 128.dp
            ),
            sheetContent = {
                val gridState = rememberLazyGridState()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .backgroundWhenScrolled(
                            gridState,
                            MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                            MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                        )
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    SingleChoiceSegmentedButtonRow {
                        SegmentedButton(
                            selected = departureType == DepartureType.Outward,
                            onClick = { departureType = DepartureType.Outward },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                            icon = { Icon(Icons.Rounded.Attractions, null) }
                        ) {
                            Text("Hinfahrt")
                        }
                        SegmentedButton(
                            selected = departureType == DepartureType.Return,
                            onClick = { departureType = DepartureType.Return },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                            icon = { Icon(Icons.Rounded.Home, null) }
                        ) {
                            Text("Rückfahrt")
                        }
                    }
                    Row {
                        when (departureType) {
                            DepartureType.Outward -> stationState.outwardTimetable
                            DepartureType.Return -> stationState.returnTimetable
                        }.departureDays.forEach {
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
                    timetable = when (departureType) {
                        DepartureType.Outward -> stationState.outwardTimetable
                        DepartureType.Return -> stationState.returnTimetable
                    },
                    gridState = gridState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding)
                )
            },
            topBar = {
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
            }
        ) {
            Column(
                Modifier.padding(it)
            ) {
                Column(
                    Modifier
                        .onGloballyPositioned {
                            contentHeight = with(density) { it.size.height.toDp() }
                        },
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
