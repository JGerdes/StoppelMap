@file:OptIn(
    ExperimentalLifecycleComposeApi::class,
    ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class,
    ExperimentalLifecycleComposeApi::class,
)

package com.jonasgerdes.stoppelmap.transportation.ui.station

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.theme.components.LoadingSpinner
import com.jonasgerdes.stoppelmap.transportation.R
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf

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
                            stringResource(id = R.string.transportation_route_topbar_navigateBack_contentDescription)
                        )
                    }
                }
            )
        }
    } else {
        LoadingSpinner(Modifier.fillMaxSize())
    }
}
