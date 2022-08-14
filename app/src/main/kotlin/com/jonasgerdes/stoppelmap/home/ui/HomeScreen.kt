@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalLifecycleComposeApi::class
)

package com.jonasgerdes.stoppelmap.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.countdown.ui.components.CountDownWidgetSuggestionCard
import com.jonasgerdes.stoppelmap.countdown.ui.components.CountdownCard
import com.jonasgerdes.stoppelmap.schedule.GetNextOfficialEventUseCase
import com.jonasgerdes.stoppelmap.schedule.ui.components.NextOfficialEventCard
import com.jonasgerdes.stoppelmap.theme.modifier.elevationWhenScrolled
import org.koin.androidx.compose.viewModel

@SuppressLint("NewApi")
@Composable
fun HomeScreen(
    onAboutOptionTap: () -> Unit,
    onUrlTap: (String) -> Unit,
    modifier: Modifier = Modifier,
    lazyViewModel: Lazy<HomeViewModel> = viewModel(),
) {
    val viewModel by lazyViewModel
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        var showOptionsMenu by remember { mutableStateOf(false) }
        val listState = rememberLazyListState()
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.home_topbar_title)) },
            actions = {
                DropdownMenu(
                    expanded = showOptionsMenu,
                    onDismissRequest = { showOptionsMenu = false }) {
                    DropdownMenuItem(
                        leadingIcon = { Icon(Icons.Rounded.Info, contentDescription = null) },
                        text = { Text(stringResource(R.string.home_optionsMenu_about)) },
                        onClick = { onAboutOptionTap() }
                    )
                }
                IconButton(onClick = { showOptionsMenu = true }) {
                    Icon(
                        Icons.Rounded.MoreVert,
                        stringResource(R.string.home_optionsMenu_contentDescription)
                    )
                }
            },
            modifier = Modifier.elevationWhenScrolled(listState)
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            when (val nextOfficialEvent = state.nextOfficialEventState) {
                GetNextOfficialEventUseCase.Result.None -> Unit
                is GetNextOfficialEventUseCase.Result.Some -> {
                    item {
                        NextOfficialEventCard(
                            event = nextOfficialEvent.event,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            when (val countDownState = state.openingCountDownState) {
                is HomeViewModel.CountDownState.CountingDown -> item {
                    if (countDownState.showCurrentSeasonIsOverHint) {
                        CountdownCurrentSeasonIsOverHint(modifier = Modifier.fillMaxWidth()) {
                            CountdownCard(
                                days = countDownState.daysLeft,
                                hours = countDownState.hoursLeft,
                                minutes = countDownState.minutesLeft,
                                seasonYear = countDownState.year
                            )
                        }
                    } else {
                        CountdownCard(
                            days = countDownState.daysLeft,
                            hours = countDownState.hoursLeft,
                            minutes = countDownState.minutesLeft,
                            seasonYear = countDownState.year
                        )
                    }
                }
                HomeViewModel.CountDownState.Loading -> Unit
                HomeViewModel.CountDownState.Over -> Unit
            }
            when (state.countdownWidgetSuggestionState) {
                HomeViewModel.CountDownWidgetSuggestionState.Hidden -> Unit
                HomeViewModel.CountDownWidgetSuggestionState.Visible -> item {
                    CountDownWidgetSuggestionCard()
                }
            }

            item {
                Card {
                    Column(
                        Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp, bottom = 8.dp)
                    ) {
                        Text(
                            text = "Du willst auf dem Laufenden bleiben, was StoppelMap und den Stoppelmarkt angeht?",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Button(
                            onClick = { onUrlTap("https://instagram.com/stoppelmap") },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Folge StoppelMap auf Instagram")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CountdownCurrentSeasonIsOverHint(modifier: Modifier, content: @Composable () -> Unit) {
    Column(
        modifier
    ) {
        Text(
            text = stringResource(id = R.string.home_countdownCard_nextSeason_title),
        )
        Spacer(modifier = Modifier.size(8.dp))
        content()
    }
}
