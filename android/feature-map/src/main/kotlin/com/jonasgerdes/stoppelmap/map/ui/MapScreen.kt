package com.jonasgerdes.stoppelmap.map.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationSearching
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.components.Map
import com.jonasgerdes.stoppelmap.map.model.PermissionState.Granted
import com.jonasgerdes.stoppelmap.map.ui.components.MapBottomSheetContent
import com.jonasgerdes.stoppelmap.map.ui.components.SearchView
import com.jonasgerdes.stoppelmap.map.ui.components.SuggestionRow
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
            MapBottomSheetContent(
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