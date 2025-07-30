package com.jonasgerdes.stoppelmap.map.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.map.model.FullMapEntity
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel

@Composable
fun MapBottomSheetContent(
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
                    verticalArrangement = Arrangement.Absolute.spacedBy(8.dp),
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
                    verticalArrangement = Arrangement.Absolute.spacedBy(8.dp),
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