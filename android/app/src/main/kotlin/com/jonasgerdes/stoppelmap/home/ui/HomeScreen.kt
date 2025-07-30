@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.jonasgerdes.stoppelmap.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.countdown.model.CountDownState
import com.jonasgerdes.stoppelmap.countdown.ui.components.CountDownWidgetSuggestionCard
import com.jonasgerdes.stoppelmap.countdown.ui.components.CountdownCard
import com.jonasgerdes.stoppelmap.home.components.MessageCard
import com.jonasgerdes.stoppelmap.home.components.NextOfficialEventCard
import com.jonasgerdes.stoppelmap.theme.spacing.defaultContentPadding
import com.jonasgerdes.stoppelmap.update.model.UpdateState
import com.jonasgerdes.stoppelmap.update.ui.AppUpdateViewModel
import com.jonasgerdes.stoppelmap.update.ui.components.AppUpdateCard
import org.koin.androidx.compose.koinViewModel

@SuppressLint("NewApi")
@Composable
fun HomeScreen(
    onSettingsOptionTap: () -> Unit,
    onUrlTap: (String) -> Unit,
    onCallPhoneNumber: (String) -> Unit,
    onDownloadUpdateTap: (AppUpdateInfo) -> Unit,
    onOpenGooglePlayTap: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    updateAppViewModel: AppUpdateViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val updateState by updateAppViewModel.state.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.home_topbar_title))
                },
                actions = {
                    IconButton(onClick = onSettingsOptionTap) {
                        Icon(
                            Icons.Rounded.Settings,
                            stringResource(R.string.home_toolbar_action_settings_contentDescription)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        LazyColumn(
            contentPadding = defaultContentPadding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            state.messages.forEach { message ->
                item {
                    MessageCard(
                        message = message,
                        onUrlTap = onUrlTap,
                    )
                }
            }
            if (updateState.updateState !is UpdateState.Hidden) {
                item {
                    AppUpdateCard(
                        updateState.updateState,
                        onDownloadTap = onDownloadUpdateTap,
                        onInstallTap = updateAppViewModel::onCompleteAppUpdateTapped,
                        onOpenGooglePlayTap = onOpenGooglePlayTap,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            when (val countDownState = state.openingCountDownState) {
                is CountDownState.CountingDown -> item {
                    if (countDownState.showCurrentSeasonIsOverHint) {
                        CountdownCurrentSeasonIsOverHint(modifier = Modifier.fillMaxWidth()) {
                            CountdownCard(
                                days = countDownState.daysLeft,
                                hours = countDownState.hoursLeft,
                                minutes = countDownState.minutesLeft,
                                seconds = countDownState.secondsLeft,
                                season = countDownState.season,
                            )
                        }
                    } else {
                        CountdownCard(
                            days = countDownState.daysLeft,
                            hours = countDownState.hoursLeft,
                            minutes = countDownState.minutesLeft,
                            seconds = countDownState.secondsLeft,
                            season = countDownState.season,
                        )
                    }
                }

                CountDownState.Loading -> Unit
                CountDownState.Over -> Unit
            }
            val promotedEventsState = state.promotedEventsState
            if (promotedEventsState is HomeViewModel.PromotedEventsState.Visible) {
                item {
                    Text(
                        text = stringResource(id = R.string.home_officalEventCard_title),
                    )
                }
                itemsIndexed(
                    promotedEventsState.events,
                    key = { _, event -> event.slug }
                ) { index, event ->
                    NextOfficialEventCard(
                        event = event,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = when {
                                    promotedEventsState.events.size == 1 -> 0.dp
                                    index == promotedEventsState.events.lastIndex -> 16.dp
                                    else -> 0.dp
                                }
                            )
                    )
                }
            }
            when (state.countdownWidgetSuggestionState) {
                HomeViewModel.CountDownWidgetSuggestionState.Hidden -> Unit
                HomeViewModel.CountDownWidgetSuggestionState.Visible -> item {
                    CountDownWidgetSuggestionCard()
                }
            }

            val panamaState = state.panamaState
            if (panamaState is HomeViewModel.PanamaState.Visible) {
                item {
                    Card {
                        Column(
                            Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 16.dp, bottom = 8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.panama_card_title),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedButton(
                                onClick = { onUrlTap(panamaState.url) },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = stringResource(R.string.panama_card_button_url))
                            }
                            Spacer(modifier = Modifier.size(8.dp))
                            Button(
                                onClick = { onCallPhoneNumber(panamaState.policeNumber) },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = stringResource(R.string.panama_card_button_police))
                            }
                            Spacer(modifier = Modifier.size(8.dp))
                            Button(
                                onClick = { onCallPhoneNumber(panamaState.medicalNumber) },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = stringResource(R.string.panama_card_button_medical))
                            }
                        }
                    }
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
                                text = stringResource(R.string.social_media_promo_instagram_title),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            Button(
                                onClick = { onUrlTap("https://instagram.com/stoppelmap") },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = stringResource(R.string.social_media_promo_instagram_button))
                            }
                        }
                    }
                }
            }

            val feedbackState = state.feedbackState
            if (feedbackState is HomeViewModel.FeedbackState.Visible) {
                item {
                    Card {
                        Column(
                            Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 16.dp, bottom = 8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.feedback_card_title),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            Button(
                                onClick = { onUrlTap(feedbackState.url) },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = stringResource(R.string.feedback_card_button))
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
