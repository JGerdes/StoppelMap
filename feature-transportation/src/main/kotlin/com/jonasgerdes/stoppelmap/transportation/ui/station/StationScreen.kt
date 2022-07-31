@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLifecycleComposeApi::class, ExperimentalLifecycleComposeApi::class,
)

package com.jonasgerdes.stoppelmap.transportation.ui.station

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.theme.components.ListLineHeader
import com.jonasgerdes.stoppelmap.theme.components.LoadingSpinner
import com.jonasgerdes.stoppelmap.transportation.R
import com.jonasgerdes.stoppelmap.transportation.model.Timetable
import kotlinx.datetime.toJavaLocalTime
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("NewApi")
@Composable
fun StationScreen(
    stationId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    lazyViewModel: Lazy<StationViewModel> = viewModel { parametersOf(stationId) }
) {
    val viewModel by lazyViewModel
    val state by viewModel.state.collectAsStateWithLifecycle()


    val stationState = state.stationState
    if (stationState is StationViewModel.StationState.Loaded) {
        Column(
            modifier = modifier
        ) {
            CenterAlignedTopAppBar(
                title = { Text(text = stationState.stationTitle) },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigateBack() }
                    ) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            stringResource(id = R.string.transportation_station_topbar_navigateBack_contentDescription)
                        )
                    }
                }
            )
            val topShape = remember {
                RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            }
            Card(
                shape = topShape,
                modifier = Modifier
                    .padding(horizontal = 8.dp),
            ) {
                Column(modifier.fillMaxWidth()) {
                    val gridState = rememberLazyGridState()
                    val firstItemVisible by remember {
                        derivedStateOf {
                            gridState.firstVisibleItemIndex == 0
                        }
                    }
                    val elevation by animateDpAsState(
                        targetValue = if (firstItemVisible) 0.dp else 4.dp
                    )
                    Card(
                        shape = topShape,
                        modifier = Modifier.shadow(elevation)
                    ) {
                        Row {
                            stationState.timetable.departureDays.forEach {
                                Text(
                                    text = stringResource(it.dayOfWeek.toResourceString()),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .padding(16.dp)
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
        timetable.segments.forEach {
            item(span = { GridItemSpan(maxLineSpan) }) {
                ListLineHeader(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(it.type.toStringResource()),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            items(it.departureSlots.flatMap { it.departures }) { slot ->
                if (slot != null) {
                    Text(
                        text = slot.time.time.toJavaLocalTime().format(timeFormatter),
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

private fun Timetable.DaySegmentType.toStringResource() = when (this) {
    Timetable.DaySegmentType.MORNING -> R.string.transportation_station_timetable_segment_morning
    Timetable.DaySegmentType.AFTERNOON -> R.string.transportation_station_timetable_segment_afternoon
    Timetable.DaySegmentType.EVENING -> R.string.transportation_station_timetable_segment_evening
    Timetable.DaySegmentType.NIGHT -> R.string.transportation_station_timetable_segment_night
}

private fun DayOfWeek.toResourceString() = when (this) {
    DayOfWeek.MONDAY -> R.string.transportation_station_timetable_day_monday
    DayOfWeek.TUESDAY -> R.string.transportation_station_timetable_day_tuesday
    DayOfWeek.WEDNESDAY -> R.string.transportation_station_timetable_day_wednesday
    DayOfWeek.THURSDAY -> R.string.transportation_station_timetable_day_thursday
    DayOfWeek.FRIDAY -> R.string.transportation_station_timetable_day_friday
    DayOfWeek.SATURDAY -> R.string.transportation_station_timetable_day_saturday
    DayOfWeek.SUNDAY -> R.string.transportation_station_timetable_day_sunday
}
