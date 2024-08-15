package com.jonasgerdes.stoppelmap.map.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

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
        initialValue = SheetValue.PartiallyExpanded,
        confirmValueChange = {
            when (state.bottomSheetState) {
                MapViewModel.BottomSheetState.Hidden -> false
                is MapViewModel.BottomSheetState.Idle -> it != SheetValue.Hidden
                is MapViewModel.BottomSheetState.Collection -> true
                is MapViewModel.BottomSheetState.SingleStall -> true
            }
        },
        skipHiddenState = state.bottomSheetState is MapViewModel.BottomSheetState.Idle,
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

            val searchQuery = rememberSaveable { mutableStateOf("") }
            val searchIsActive = rememberSaveable { mutableStateOf(false) }
            AnimatedVisibility(visible = searchIsActive.value, enter = fadeIn(), exit = fadeOut()) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp))
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding(),
            ) {
                SearchBar(
                    leadingIcon = {
                        AnimatedContent(targetState = searchIsActive.value) { isActive ->
                            if (!isActive) Icon(Icons.Rounded.Search, null)
                            else IconButton(onClick = { searchIsActive.value = false }) {
                                Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                            }
                        }
                    },
                    trailingIcon = {
                        val showClear by remember { derivedStateOf { searchQuery.value.isNotEmpty() } }
                        AnimatedVisibility(visible = showClear, enter = fadeIn(), exit = fadeOut()) {
                            IconButton(onClick = {
                                searchQuery.value = ""
                                viewModel.onSearch("")
                            }) {
                                Icon(Icons.Rounded.Clear, null)
                            }
                        }
                    },
                    placeholder = { Text(stringResource(R.string.map_search_placeholder)) },
                    query = searchQuery.value,
                    onQueryChange = {
                        searchQuery.value = it
                        viewModel.onSearch(it)
                    },
                    onSearch = {},
                    active = searchIsActive.value,
                    onActiveChange = {
                        searchIsActive.value = it
                    }
                ) {
                    AnimatedContent(targetState = state.searchState.inProgress) { searchInProgress ->
                        if (searchInProgress) LinearProgressIndicator(Modifier.fillMaxWidth())
                        else Spacer(modifier = Modifier.height(4.dp))
                    }
                    LazyColumn {
                        items(
                            state.searchState.results,
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
                                modifier = Modifier.clickable {
                                    searchIsActive.value = false
                                    searchQuery.value = result.term
                                    viewModel.onSearchResultTap(result)
                                }
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible =
                    (state.bottomSheetState is MapViewModel.BottomSheetState.Idle || state.bottomSheetState is MapViewModel.BottomSheetState.Hidden)
                            && state.searchState.quickSearchChips.isNotEmpty()
                ) {

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 32.dp),
                        horizontalArrangement = spacedBy(4.dp)
                    ) {
                        items(
                            state.searchState.quickSearchChips,
                            key = { it.term + it.resultEntities.joinToString { it.slug } }) { suggestion ->
                            ElevatedSuggestionChip(
                                shape = RoundedCornerShape(100),
                                onClick = { viewModel.onSearchResultTap(suggestion) },
                                label = { Text(suggestion.term) },
                                icon = {
                                    suggestion.icon?.iconRes?.let { res ->
                                        Icon(
                                            painterResource(id = res),
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
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
