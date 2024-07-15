@file:OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)

package com.jonasgerdes.stoppelmap.map.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import com.jonasgerdes.stoppelmap.map.components.Map
import com.jonasgerdes.stoppelmap.theme.components.NoticeCard
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

    val searchState = state.searchState

    Box(modifier = modifier) {
        Map(
            mapState = state.mapState,
            onCameraUpdateDispatched = viewModel::onCameraUpdateDispatched,
            onCameraMoved = viewModel::onCameraMoved,
            onStallTap = viewModel::onStallTapped,
            mapDataFile = "file://${mapDataFile.file.absolutePath}".also { Timber.d("mapFile: $it") },
            colors = state.mapTheme.toMapColors(),
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        )
        NoticeCard(
            title = "Stand 2023",
            message = "Ein Update für 2024 ist in Arbeit",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding(),
        )
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .align(Alignment.BottomStart)
                .fillMaxWidth()
        ) {
            FloatingActionButton(
                onClick = {
                    onRequestLocationPermission()
                    viewModel.onMyLocationFabTap()
                },
                shape = CircleShape,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.End)
            ) {
                Icon(Icons.Rounded.MyLocation, contentDescription = "Lokalisiere mich")
            }

            BackHandler(enabled = searchState !is MapViewModel.SearchState.Collapsed) {
                viewModel.onCloseSearchTapped()
            }

            AnimatedVisibility(
                visible = searchState is MapViewModel.SearchState.HighlightResult,
                modifier = Modifier.fillMaxWidth()
            ) {
                ElevatedCard(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 16.dp, top = 8.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                ) {
                    if (searchState is MapViewModel.SearchState.HighlightResult) {
                        Column(
                            Modifier
                                .padding(8.dp)
                                .padding(bottom = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = searchState.result.term,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 8.dp,
                                            vertical = 8.dp
                                        )
                                        .weight(1f)
                                )
                                IconButton(
                                    onClick = { viewModel.onCloseSearchTapped() },
                                ) {
                                    Icon(Icons.Rounded.Close, contentDescription = "Schließen")
                                }
                            }
                            if (searchState.result.stalls.size > 1) {
                                Text(
                                    text = "${searchState.result.stalls.size} mal auf dem Stoppelmarkt",
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }

                    }
                }
            }
        }

        AnimatedVisibility(
            visible = searchState !is MapViewModel.SearchState.HighlightResult,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Search(
                query = (searchState as? MapViewModel.SearchState.Search)?.query ?: "",
                results = (searchState as? MapViewModel.SearchState.Search)?.results
                    ?: emptyList(),
                searchState = if (searchState is MapViewModel.SearchState.Search) SearchState.Expanded else SearchState.Collapsed,
                onSearchButtonTap = viewModel::onSearchButtonTapped,
                onCloseSearchTap = viewModel::onCloseSearchTapped,
                onQueryChange = viewModel::onSearchQueryChanged,
                onResultTap = viewModel::onSearchResultTapped,
                collapsedPaddingBottom = 56.dp + 32.dp + scaffoldPadding.calculateBottomPadding(),
            )
        }

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
                    .statusBarsPadding()
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
