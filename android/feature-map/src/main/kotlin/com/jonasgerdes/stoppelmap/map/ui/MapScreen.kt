package com.jonasgerdes.stoppelmap.map.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.LocationSearching
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.components.Map
import com.jonasgerdes.stoppelmap.map.model.FullMapEntity
import com.jonasgerdes.stoppelmap.map.model.PermissionState.Granted
import com.jonasgerdes.stoppelmap.map.model.SearchResult
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onRequestLocationPermission: () -> Unit,
    viewModel: MapViewModel = koinViewModel(),
    mapColorViewModel: MapColorViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val mapTheme by mapColorViewModel.mapColorState.collectAsStateWithLifecycle()

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false,
    )
    LaunchedEffect(state.bottomSheetState) {
        when (state.bottomSheetState) {
            MapViewModel.BottomSheetState.Hidden -> bottomSheetState.hide()
            is MapViewModel.BottomSheetState.Idle -> bottomSheetState.requireOffset()
            is MapViewModel.BottomSheetState.Collection -> bottomSheetState.show()
            is MapViewModel.BottomSheetState.SingleStall -> bottomSheetState.show()
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState,
        snackbarHostState = snackbarHostState,
    )
    val bottomSheetMainContentHeight = remember { mutableStateOf(BottomSheetDefaults.SheetPeekHeight) }
    val bottomSheetMainContentHeightAnimated = animateDpAsState(targetValue = bottomSheetMainContentHeight.value)

    val locationFabBottomPadding = animateDpAsState(
        targetValue =
            if (bottomSheetState.targetValue != SheetValue.Hidden) bottomSheetMainContentHeight.value + BottomSheetDefaults.SheetPeekHeight
            else 0.dp
    )

    val mapPadding = PaddingValues(
        start = 32.dp,
        end = 32.dp,
        top = 56.dp /* = DockedHeaderContainerHeight*/ + 32.dp
                + WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                + 32.dp /* = SuggestionChipTokens.ContainerHeight */,

        bottom = bottomSheetMainContentHeight.value + 32.dp
    )

    LaunchedEffect(bottomSheetState.isVisible) {
        if (!bottomSheetState.isVisible) {
            viewModel.onBottomSheetClose()
        }
    }

    val notInAreaString = stringResource(id = R.string.map_location_not_in_area)
    LaunchedEffect(state.locationState.showNotInAreaHint) {
        if (state.locationState.showNotInAreaHint) {
            snackbarHostState.showSnackbar(notInAreaString, duration = SnackbarDuration.Long)
            viewModel.onNotInAreaHintShow()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            SheetContent(
                onMainContentHeightChange = {
                    bottomSheetMainContentHeight.value = it
                },
                bottomSheetState = state.bottomSheetState
            )
        },
        sheetPeekHeight = bottomSheetMainContentHeightAnimated.value +
                BottomSheetDefaults.SheetPeekHeight, // account for handle etc
        modifier = modifier,
    ) {
        var showMap by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            delay(200.milliseconds)
            showMap = true
        }
        Box {
            val mapDataPath = state.mapDataPath
            if (mapDataPath != null) {
                Map(
                    onCameraUpdateDispatched = { viewModel.onCameraUpdateDispatched() },
                    onCameraMoved = { viewModel.onCameraMoved() },
                    onMapTap = {
                        viewModel.onMapTap(it)
                    },
                    mapDataFile = "file://$mapDataPath".also { Timber.d("mapFile: $it") },
                    colors = mapTheme.toMapColors(),
                    mapState = state.mapState,
                    padding = mapPadding,
                    modifier = Modifier.fillMaxSize()
                )
            }
            AnimatedVisibility(!showMap, exit = fadeOut()) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                )
            }
            FloatingActionButton(
                onClick = {
                    if (state.locationState.permissionState == Granted) viewModel.onLocationButtonTap()
                    else {
                        viewModel.requestLocationPermission()
                        onRequestLocationPermission()
                    }
                },
                content = {
                    Icon(
                        if (state.locationState.isFollowingLocation) Icons.Rounded.MyLocation
                        else Icons.Rounded.LocationSearching,
                        contentDescription = stringResource(id = R.string.map_location_button_contentDescription),
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = locationFabBottomPadding.value)
                    .padding(16.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                var searchExpanded by rememberSaveable { mutableStateOf(false) }
                SearchView(
                    expanded = searchExpanded,
                    onExpandedChange = { searchExpanded = it },
                    searchState = state.searchState,
                    onSearch = viewModel::onSearch,
                    onSearchResultTap = viewModel::onSearchResultTap,
                )
                val showSuggestions by remember(bottomSheetState, state.searchState) {
                    derivedStateOf {
                        bottomSheetState.targetValue != SheetValue.Expanded && state.searchState.quickSearchChips.isNotEmpty()
                    }
                }
                AnimatedVisibility(
                    visible = showSuggestions,
                ) {
                    SuggestionRow(
                        suggestions = state.searchState.quickSearchChips,
                        onSuggestionSelected = viewModel::onSearchResultTap,
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchView(
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

@Composable
private fun SuggestionRow(
    suggestions: List<SearchResult>,
    onSuggestionSelected: (SearchResult) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 32.dp),
        horizontalArrangement = spacedBy(4.dp)
    ) {
        items(
            suggestions,
            key = { it.term + it.resultEntities.joinToString { it.slug } }) { suggestion ->
            ElevatedSuggestionChip(
                shape = RoundedCornerShape(100),
                onClick = { onSuggestionSelected(suggestion) },
                label = { Text(suggestion.term) },
                icon = {
                    suggestion.icon?.iconRes?.let { res ->
                        Icon(
                            painterResource(id = res),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )
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

@Composable
private fun SearchLoadingIndicator(inProgress: Boolean) {
    AnimatedVisibility(
        visible = inProgress,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        var progressTarget by remember { mutableFloatStateOf(0f) }
        val progress by animateFloatAsState(
            targetValue = progressTarget,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        )
        LinearProgressIndicator(
            progress = { progress },
            trackColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        )
        LaunchedEffect(Unit) {
            progressTarget = 0.7f
            delay(500)
            progressTarget = 0.8f
            delay(1000)
            progressTarget = 0.9f
        }
    }
}

@Composable
private fun SheetContent(
    modifier: Modifier = Modifier,
    bottomSheetState: MapViewModel.BottomSheetState,
    onMainContentHeightChange: (Dp) -> Unit,
) {
    val density = LocalDensity.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        when (bottomSheetState) {
            MapViewModel.BottomSheetState.Hidden -> Unit
            is MapViewModel.BottomSheetState.Idle -> Unit
            is MapViewModel.BottomSheetState.Collection -> {
                Column(
                    verticalArrangement = spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            with(density) {
                                onMainContentHeightChange(
                                    it.size.height.toDp()
                                )
                            }
                        }
                ) {
                    Text(text = bottomSheetState.name, style = MaterialTheme.typography.headlineLarge)
                    Text(text = bottomSheetState.subline(), style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            is MapViewModel.BottomSheetState.SingleStall -> {
                val mapEntity = bottomSheetState.fullMapEntity
                Column(
                    verticalArrangement = spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            with(density) {
                                onMainContentHeightChange(
                                    it.size.height.toDp() + if (mapEntity.hasRichContent()) 64.dp else 0.dp
                                )
                            }
                        }
                ) {
                    Text(text = mapEntity.name, style = MaterialTheme.typography.headlineLarge)
                    mapEntity.subline()?.let {
                        Text(text = it, style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                mapEntity.description?.let {
                    Text(it)
                }
            }
        }
    }

}

private fun FullMapEntity.hasRichContent() = description != null
