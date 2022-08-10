@file:OptIn(
    ExperimentalLifecycleComposeApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)

package com.jonasgerdes.stoppelmap.map.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel.SearchState
import org.koin.androidx.compose.viewModel

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
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
            onQueryChange = viewModel::onSearchQueryChanged,
            onResultTap = viewModel::onSearchResultTapped
        )

    }
}

@Composable
fun Search(
    searchState: SearchState,
    onQueryChange: (String) -> Unit,
    onResultTap: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchExpanded by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    ElevatedCard(
        modifier = modifier.padding(16.dp)
    ) {
        AnimatedContent(targetState = searchExpanded) { targetExpanded ->
            if (targetExpanded) {
                Column(Modifier.fillMaxWidth()) {
                    TextField(
                        value = searchState.query,
                        onValueChange = onQueryChange,
                        trailingIcon = {
                            IconButton(onClick = {
                                searchExpanded = false
                                onQueryChange("")
                            }) {
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
                                        onResultTap(result.slug)
                                        searchExpanded = false
                                    }
                                    .padding(16.dp)
                                ) {
                                    val context = LocalContext.current
                                    val vectorId =
                                        context.resources.getIdentifier(
                                            "ic_stall_type_${result.type}",
                                            "drawable",
                                            context.packageName
                                        )
                                    Icon(
                                        painterResource(id = vectorId),
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(text = result.name!!)
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
            } else {
                Row(Modifier
                        .clickable { searchExpanded = true }
                        .padding(vertical = 8.dp, horizontal = 16.dp)) {
                    Icon(Icons.Rounded.Search, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Suche")
                }
            }
        }
    }
}
