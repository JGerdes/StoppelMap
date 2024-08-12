package com.jonasgerdes.stoppelmap.map.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.map.components.Map
import com.jonasgerdes.stoppelmap.map.components.MapTheme
import com.jonasgerdes.stoppelmap.map.model.SearchResult
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    scaffoldPadding: PaddingValues,
    onRequestLocationPermission: () -> Unit,
    viewModel: MapViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        confirmValueChange = { it != SheetValue.Hidden },
        skipHiddenState = true,
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )
    val scope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            SheetContent(modifier = Modifier.padding(scaffoldPadding), state.bottomSheetState)
        },
        sheetPeekHeight = BottomSheetDefaults.SheetPeekHeight + scaffoldPadding.calculateBottomPadding(),
        modifier = modifier,
    ) {
        Box {
            val mapDataPath = state.mapDataPath
            Timber.d("mapDataPath: $mapDataPath")
            if (mapDataPath != null) {
                Map(
                    onCameraUpdateDispatched = { viewModel.onCameraUpdateDispatched() },
                    onCameraMoved = { viewModel.onCameraMoved() },
                    onStallTap = {
                        viewModel.onMapTap(it)
                    },
                    mapDataFile = "file://$mapDataPath".also { Timber.d("mapFile: $it") },
                    colors = MapTheme().toMapColors(),
                    mapState = state.mapState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(scaffoldPadding)
                )
            }
            val searchQuery = rememberSaveable { mutableStateOf("") }
            val searchIsActive = rememberSaveable { mutableStateOf(false) }
            AnimatedVisibility(visible = searchIsActive.value, enter = fadeIn(), exit = fadeOut()) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp))
                )
            }
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
                query = searchQuery.value,
                onQueryChange = {
                    searchQuery.value = it
                    viewModel.onSearch(it)
                },
                onSearch = {},
                active = searchIsActive.value,
                onActiveChange = {
                    Timber.d("onActiveChange: $it")
                    searchIsActive.value = it
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
            ) {
                AnimatedContent(targetState = state.searchState.inProgress) { searchInProgress ->
                    if (searchInProgress) LinearProgressIndicator(Modifier.fillMaxWidth())
                    else Spacer(modifier = Modifier.height(4.dp))
                }
                state.searchState.results.forEach { result ->
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
                            when (result.type) {
                                SearchResult.Type.SingleStall -> result.resultEntities.first().typeName?.let { type ->
                                    val subType = result.resultEntities.first().subTypeName
                                    Text(if (subType == null) type else "$subType ($type)")
                                }
                            }
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
    }
}

@Composable
private fun SheetContent(modifier: Modifier = Modifier, bottomSheetState: MapViewModel.BottomSheetState) {
    when (bottomSheetState) {
        MapViewModel.BottomSheetState.Hidden -> Unit
        is MapViewModel.BottomSheetState.Idle -> Unit
        is MapViewModel.BottomSheetState.SearchResult -> TODO()
        is MapViewModel.BottomSheetState.SingleStall -> {
            val mapEntity = bottomSheetState.fullMapEntity
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.Red)
            ) {
                mapEntity.name?.let {
                    Text(text = it, style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }

}
