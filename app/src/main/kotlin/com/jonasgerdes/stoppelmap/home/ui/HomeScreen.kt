@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalLifecycleComposeApi::class
)

package com.jonasgerdes.stoppelmap.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.home.ui.components.CountdownCard
import org.koin.androidx.compose.viewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    lazyViewModel: Lazy<HomeViewModel> = viewModel(),
) {
    val viewModel by lazyViewModel
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.home_topbar_title)) })
        LazyColumn(
            contentPadding = PaddingValues(16.dp), modifier = Modifier.fillMaxSize()
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

        }
    }
}
