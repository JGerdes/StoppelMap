@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalLifecycleComposeApi::class
)

package com.jonasgerdes.stoppelmap.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.countdown.ui.components.CountDownWidgetSuggestionCard
import com.jonasgerdes.stoppelmap.countdown.ui.components.CountdownCard
import com.jonasgerdes.stoppelmap.theme.modifier.elevationWhenScrolled
import org.koin.androidx.compose.viewModel

@SuppressLint("NewApi")
@Composable
fun HomeScreen(
    onAboutOptionTap: () -> Unit,
    onUrlTap: (url: String) -> Unit,
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
            when (val countDownState = state.openingCountDownState) {
                is HomeViewModel.CountDownState.CountingDown -> item {
                    CountdownCard(
                        days = countDownState.daysLeft,
                        hours = countDownState.hoursLeft,
                        minutes = countDownState.minutesLeft
                    )
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
        }
    }
}
