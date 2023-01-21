@file:OptIn(
    ExperimentalLifecycleComposeApi::class, ExperimentalAnimationApi::class,
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onRequestLocationPermission: () -> Unit,
    viewModel: MapViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier) {
        MapboxMap(
            mapState = state.mapState,
            onCameraMove = viewModel::onCameraMoved,
            onStallTap = viewModel::onStallTapped,
            modifier = Modifier.fillMaxSize()
        )
        FloatingActionButton(
            onClick = {
                onRequestLocationPermission()
                viewModel.onMyLocationFabTap()
            },
            shape = CircleShape,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Rounded.MyLocation, contentDescription = "Lokalisiere mich")
        }

        Search(
            searchState = state.searchState,
            onSearchButtonTap = viewModel::onSearchButtonTapped,
            onCloseSearchTap = viewModel::onCloseSearchTapped,
            onQueryChange = viewModel::onSearchQueryChanged,
            onResultTap = viewModel::onSearchResultTapped
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
        val vectorId = context.getDrawableForStallType(searchState.result.stalls.first().type)
        if (vectorId == null) {
            Spacer(modifier = Modifier.size(24.dp))
        } else {
            Icon(
                painterResource(id = vectorId),
                contentDescription = null
            )
        }
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onResultTap(result)
                            }
                            .padding(16.dp)
                    ) {
                        val context = LocalContext.current
                        val vectorId = context.getDrawableForStallType(result.stalls.first().type)
                        if (vectorId == null) {
                            Spacer(modifier = Modifier.size(24.dp))
                        } else {
                            Icon(
                                painterResource(id = vectorId),
                                contentDescription = null
                            )
                        }
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

@DrawableRes
private fun Context.getDrawableForStallType(stallType: String): Int? =
    "ic_stall_type_${stallType.replace("-", "_")}".let { name ->
        val id = resources.getIdentifier(
            name,
            "drawable",
            packageName
        )
        if (id == 0) {
            Timber.w("Can't find icon for type [$stallType]")
            null
        } else id
    }
