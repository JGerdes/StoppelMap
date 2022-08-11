@file:OptIn(
    ExperimentalLifecycleComposeApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)

package com.jonasgerdes.stoppelmap.map.ui

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.map.components.MapboxMap
import com.jonasgerdes.stoppelmap.map.model.SearchResult
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel.SearchState
import org.koin.androidx.compose.viewModel

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onRequestLocationPermission: () -> Unit,
    lazyViewModel: Lazy<MapViewModel> = viewModel(),
) {
    val viewModel by lazyViewModel
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier) {
        MapboxMap(
            mapState = state.mapState,
            onCameraMove = viewModel::onCameraMoved,
            onStallTap = viewModel::onStallTapped,
            modifier = Modifier.fillMaxSize()
        )
        Search(
            searchState = state.searchState,
            onSearchButtonTap = viewModel::onSearchButtonTapped,
            onCloseSearchTap = viewModel::onCloseSearchTapped,
            onQueryChange = viewModel::onSearchQueryChanged,
            onResultTap = viewModel::onSearchResultTapped
        )
        Text(
            text = "Weitere Stände, Attraktionen und Zelte folgen in Kürze",
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(0.6f)
                .padding(8.dp)
        )
        FloatingActionButton(
            onClick = {
                onRequestLocationPermission()
                viewModel.onMyLocationFabTap()
            },
            shape = CircleShape,
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = 32.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Rounded.MyLocation, contentDescription = "Meine Position")
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

@Composable
fun Search(
    searchState: SearchState,
    onSearchButtonTap: () -> Unit,
    onCloseSearchTap: () -> Unit,
    onQueryChange: (String) -> Unit,
    onResultTap: (SearchResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier.padding(16.dp)
    ) {
        //AnimatedContent(targetState = searchState) { searchState ->
        when (searchState) {
            SearchState.Collapsed -> SearchPill(onTap = onSearchButtonTap)
            is SearchState.Search -> SearchField(
                searchState = searchState,
                onQueryChange = onQueryChange,
                onResultTap = onResultTap,
                onCloseSearchTap = onCloseSearchTap
            )
            is SearchState.HighlightResult -> HighlightedCard(
                searchState = searchState,
                onCloseSearchTap = onCloseSearchTap
            )
        }
        //}
    }
}

@Composable
fun HighlightedCard(
    searchState: SearchState.HighlightResult,
    onCloseSearchTap: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val context = LocalContext.current
        val vectorId =
            context.resources.getIdentifier(
                "ic_stall_type_${searchState.result.stalls.first().type}",
                "drawable",
                context.packageName
            )
        Icon(
            painterResource(id = vectorId),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = searchState.result.term)
        Spacer(
            modifier = Modifier
                .defaultMinSize(minWidth = 8.dp)
                .weight(1f)
        )
        IconButton(
            onClick = { onCloseSearchTap() }
        ) {
            Icon(Icons.Rounded.Close, contentDescription = null)
        }
    }
}

@Composable
fun SearchField(
    searchState: SearchState.Search,
    onQueryChange: (String) -> Unit,
    onResultTap: (SearchResult) -> Unit,
    onCloseSearchTap: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    Column(Modifier.fillMaxWidth()) {
        TextField(
            value = searchState.query,
            onValueChange = onQueryChange,
            trailingIcon = {
                IconButton(onClick = { onCloseSearchTap() }) {
                    Icon(Icons.Rounded.Close, contentDescription = null)
                }
            },
            placeholder = { Text("Suche") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )
        if (searchState.results.isNotEmpty() || searchState.query.isBlank()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(searchState.results) { result ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onResultTap(result)
                        }
                        .padding(16.dp)
                    ) {
                        val context = LocalContext.current
                        val vectorId =
                            context.resources.getIdentifier(
                                "ic_stall_type_${result.stalls.first().type}",
                                "drawable",
                                context.packageName
                            )
                        Icon(
                            painterResource(id = vectorId),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = result.term)
                    }
                }
            }
        } else {
            Text(
                text = "Keine Ergebnisse zur Suche nach \"${searchState.query}\"",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
        }
    }
    DisposableEffect(Unit) {
        focusRequester.requestFocus()

        onDispose {
            focusRequester.freeFocus()
        }
    }
}


@Composable
fun SearchPill(
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier
            .clickable { onTap() }
            .padding(vertical = 8.dp, horizontal = 16.dp)) {
        Icon(Icons.Rounded.Search, contentDescription = null)
        Spacer(modifier = Modifier.size(8.dp))
        Text("Suche")
    }
}
