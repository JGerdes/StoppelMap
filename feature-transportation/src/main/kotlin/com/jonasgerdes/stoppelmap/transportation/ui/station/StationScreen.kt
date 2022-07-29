@file:OptIn(
    ExperimentalLifecycleComposeApi::class,
    ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class,
)

package com.jonasgerdes.stoppelmap.transportation.ui.station

import android.annotation.SuppressLint
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

}
