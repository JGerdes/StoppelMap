@file:OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)

package com.jonasgerdes.stoppelmap.map.ui

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.base.contract.MapDataFile
import com.jonasgerdes.stoppelmap.map.components.MapboxMap
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    scaffoldPadding: PaddingValues,
    onRequestLocationPermission: () -> Unit,
    viewModel: MapViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val mapDataFile: MapDataFile = get()

    Box(modifier = modifier) {
        MapboxMap(
            mapState = state.mapState,
            onCameraMove = viewModel::onCameraMoved,
            onStallTap = viewModel::onStallTapped,
            mapDataFile = "file://${mapDataFile.mapDataFile.absolutePath}".also { Timber.d("mapFile: $it") },
            colors = state.mapTheme.toMapColors(),
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        )
        FloatingActionButton(
            onClick = {
                onRequestLocationPermission()
                viewModel.onMyLocationFabTap()
            },
            shape = CircleShape,
            modifier = Modifier
                .padding(16.dp)
                .padding(scaffoldPadding)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Rounded.MyLocation, contentDescription = "Lokalisiere mich")
        }

        Search(
            query = (state.searchState as? MapViewModel.SearchState.Search)?.query ?: "",
            results = (state.searchState as? MapViewModel.SearchState.Search)?.results
                ?: emptyList(),
            searchState = if (state.searchState is MapViewModel.SearchState.Search) SearchState.Expanded else SearchState.Collapsed,
            onSearchButtonTap = viewModel::onSearchButtonTapped,
            onCloseSearchTap = viewModel::onCloseSearchTapped,
            onQueryChange = viewModel::onSearchQueryChanged,
            onResultTap = viewModel::onSearchResultTapped,
            collapsedPaddingBottom = 56.dp + 32.dp + scaffoldPadding.calculateBottomPadding(),
            modifier = Modifier.align(Alignment.BottomEnd),
        )

        var snackbarText by remember { mutableStateOf("") }

        (state.snackbarState as? MapViewModel.SnackbarState.Shown)?.text?.let {
            snackbarText = it
        }

        AnimatedVisibility(
            visible = state.snackbarState is MapViewModel.SnackbarState.Shown,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Icon(Icons.Rounded.Error, contentDescription = null)
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        text = snackbarText,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}
