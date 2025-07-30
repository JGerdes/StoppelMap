package com.jonasgerdes.stoppelmap.map.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.model.SearchResult
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel
import com.jonasgerdes.stoppelmap.map.ui.iconRes

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchView(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    searchState: MapViewModel.SearchState,
    onSearch: (String) -> Unit,
    onSearchResultTap: (SearchResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery.value,
                onQueryChange = {
                    searchQuery.value = it
                    onSearch(it)
                },
                onSearch = {},
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                enabled = true,
                placeholder = { Text(stringResource(R.string.map_search_placeholder), maxLines = 1) },
                leadingIcon = {
                    AnimatedContent(targetState = expanded) { isActive ->
                        if (!isActive) Icon(Icons.Rounded.Search, null)
                        else IconButton(onClick = { onExpandedChange(false) }) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                        }
                    }
                },
                trailingIcon = {
                    val showClear by remember { derivedStateOf { searchQuery.value.isNotEmpty() } }
                    AnimatedVisibility(visible = showClear, enter = fadeIn(), exit = fadeOut()) {
                        IconButton(onClick = {
                            searchQuery.value = ""
                            onSearch("")
                        }) {
                            Icon(Icons.Rounded.Clear, null)
                        }
                    }
                },
            )
        },
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            SearchResults(
                searchState = searchState,
                onSearchResultTap = { result ->
                    onExpandedChange(false)
                    searchQuery.value = ""
                    onSearchResultTap(result)
                },
                modifier = Modifier.imePadding(),
            )
            SearchLoadingIndicator(inProgress = searchState.inProgress)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResults(
    searchState: MapViewModel.SearchState,
    onSearchResultTap: (SearchResult) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
            searchState.results,
            key = { it.term + it.resultEntities.joinToString { it.slug } }
        ) { result ->
            ListItem(
                headlineContent = { Text(result.term) },
                leadingContent = {
                    val iconRes = result.icon?.iconRes
                    if (iconRes != null) {
                        Icon(
                            painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.size(24.dp))
                    }
                },
                supportingContent = {
                    result.supportingText()?.let { Text(it) }
                },
                colors = ListItemDefaults.colors(containerColor = SearchBarDefaults.colors().containerColor),
                modifier = Modifier
                    .clickable {
                        onSearchResultTap(result)
                    }
                    .animateItem()
            )
        }
    }

}