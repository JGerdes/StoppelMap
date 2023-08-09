package com.jonasgerdes.stoppelmap.map.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.model.SearchResult
import kotlinx.coroutines.delay
import timber.log.Timber

@Suppress("AnimateAsStateLabel")
@Composable
fun Search(
    query: String,
    results: List<SearchResult>,
    searchState: SearchState,
    onSearchButtonTap: () -> Unit,
    onCloseSearchTap: () -> Unit,
    onQueryChange: (String) -> Unit,
    onResultTap: (SearchResult) -> Unit,
    collapsedPaddingBottom: Dp,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        when (searchState) {
            SearchState.Collapsed -> MaterialTheme.colorScheme.primaryContainer
            SearchState.Expanded -> MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)
        },
        spring(stiffness = Spring.StiffnessMediumLow)
    )
    val cornerSize by animateDpAsState(
        when (searchState) {
            SearchState.Collapsed -> 56.dp
            SearchState.Expanded -> 0.dp
        },
        spring(stiffness = Spring.StiffnessMediumLow)
    )
    val paddingBottom by animateDpAsState(
        when (searchState) {
            SearchState.Collapsed -> collapsedPaddingBottom
            SearchState.Expanded -> 0.dp
        },
        spring(stiffness = Spring.StiffnessMediumLow)
    )
    val paddingEnd by animateDpAsState(
        when (searchState) {
            SearchState.Collapsed -> 16.dp
            SearchState.Expanded -> 0.dp
        },
        spring(stiffness = Spring.StiffnessMediumLow)
    )
    val elevation by animateDpAsState(
        when (searchState) {
            SearchState.Collapsed -> 6.dp
            SearchState.Expanded -> 0.dp
        },
    )
    val focusRequester = remember { FocusRequester() }
    Box(
        modifier = modifier
            .padding(bottom = paddingBottom, end = paddingEnd)
            .shadow(elevation = elevation, RoundedCornerShape(cornerSize))
            .background(backgroundColor, RoundedCornerShape(cornerSize))
            .animateContentSize(
                spring(stiffness = Spring.StiffnessMediumLow)
            )
    ) {
        Column(
            modifier = when (searchState) {
                SearchState.Collapsed -> modifier.size(56.dp)
                SearchState.Expanded -> modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .imePadding()
            }
        ) {
            Row {
                IconButton(
                    onClick = { if (searchState == SearchState.Collapsed) onSearchButtonTap() else onCloseSearchTap() },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        if (searchState == SearchState.Collapsed) Icons.Rounded.Search else Icons.Rounded.ArrowBack,
                        contentDescription = if (searchState == SearchState.Collapsed) "Karte durchsuchen" else "Suche schließen",
                    )
                }
                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    trailingIcon = {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Rounded.Close, contentDescription = null)
                        }
                    },
                    placeholder = { Text("Auf der Karte suchen") },
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                )
            }
            Divider()
            if (results.isNotEmpty() || query.isBlank()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(results) { result ->
                        ListItem(
                            headlineContent = { Text(text = result.term) },
                            leadingContent = {
                                val context = LocalContext.current
                                val vectorId =
                                    context.getDrawableForStallType(result.stalls.first().type)
                                if (vectorId == null) {
                                    Spacer(modifier = Modifier.size(24.dp))
                                } else {
                                    Icon(
                                        painterResource(id = vectorId),
                                        contentDescription = null
                                    )
                                }
                            },
                            tonalElevation = 6.dp,
                            modifier = Modifier.clickable {
                                onResultTap(result)
                            }
                        )
                    }
                }
            } else {
                Text(
                    text = "Keine Ergebnisse zur Suche nach \"${query}\"",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                )
            }
        }
        val focusManager = LocalFocusManager.current
        LaunchedEffect(searchState, focusManager) {
            delay(200)
            when (searchState) {
                SearchState.Collapsed -> focusManager.clearFocus()
                SearchState.Expanded -> focusRequester.requestFocus()
            }
        }
        DisposableEffect(focusManager) {
            onDispose {
                focusManager.clearFocus()
            }
        }
        BackHandler(enabled = searchState == SearchState.Expanded) {
            onCloseSearchTap()
        }
    }
}

enum class SearchState {
    Collapsed, Expanded
}

@DrawableRes
private fun Context.getDrawableForStallType(stallType: String): Int? =
    when (stallType) {
        "station" -> R.drawable.ic_bus
        "platform" -> R.drawable.ic_train
        "taxi" -> R.drawable.ic_taxi
        "entrance" -> R.drawable.ic_entrance
        else ->
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
    }
