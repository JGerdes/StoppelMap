@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.jonasgerdes.stoppelmap.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.countdown.ui.components.CountDownWidgetSuggestionCard
import com.jonasgerdes.stoppelmap.countdown.ui.components.CountdownCard
import com.jonasgerdes.stoppelmap.schedule.GetNextOfficialEventUseCase
import com.jonasgerdes.stoppelmap.schedule.ui.components.NextOfficialEventCard
import com.jonasgerdes.stoppelmap.theme.modifier.elevationWhenScrolled
import com.jonasgerdes.stoppelmap.update.model.UpdateState
import com.jonasgerdes.stoppelmap.update.ui.components.AppUpdateCard
import org.koin.androidx.compose.koinViewModel

@SuppressLint("NewApi")
@Composable
fun HomeScreen(
    onAboutOptionTap: () -> Unit,
    onUrlTap: (String) -> Unit,
    onDownloadUpdateTap: (AppUpdateInfo) -> Unit,
    onOpenGooglePlayTap: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
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
            modifier = Modifier
                .elevationWhenScrolled(listState)
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.updateState !is UpdateState.Hidden) {
                item {
                    AppUpdateCard(
                        state.updateState,
                        onDownloadTap = onDownloadUpdateTap,
                        onInstallTap = viewModel::onCompleteAppUpdateTapped,
                        onOpenGooglePlayTap = onOpenGooglePlayTap,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
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

            if (state.instagramPromotionState == HomeViewModel.InstagramPromotionState.Visible) {
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
