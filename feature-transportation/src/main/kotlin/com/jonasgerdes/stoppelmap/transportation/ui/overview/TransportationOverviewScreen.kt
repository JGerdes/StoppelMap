@file:OptIn(
    ExperimentalLifecycleComposeApi::class,
    ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class,
)

package com.jonasgerdes.stoppelmap.transportation.ui.overview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.transportation.R
import org.koin.androidx.compose.viewModel

@SuppressLint("NewApi")
@Composable
fun TransportationOverviewScreen(
    modifier: Modifier = Modifier,
    lazyViewModel: Lazy<TransportationOverviewViewModel> = viewModel(),
) {
    val viewModel by lazyViewModel
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.transportation_overview_topbar_title)) },
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }
}


enum class ItemTypes {
    BusRoute
}
