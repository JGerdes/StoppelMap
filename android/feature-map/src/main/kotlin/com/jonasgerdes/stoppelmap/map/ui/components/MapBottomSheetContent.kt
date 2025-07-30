package com.jonasgerdes.stoppelmap.map.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel

@Composable
fun MapBottomSheetContent(
    bottomSheetState: MapViewModel.BottomSheetState,
    onPrimaryContentHeightChange: (Dp) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        bottomSheetState,
        transitionSpec = {
            fadeIn().togetherWith(fadeOut())
        }
    ) { state ->
        when (state) {
            MapViewModel.BottomSheetState.Hidden -> Unit
            is MapViewModel.BottomSheetState.Idle -> Unit
            is MapViewModel.BottomSheetState.Collection -> {
                SheetContent(
                    onPrimaryContentHeightChange = onPrimaryContentHeightChange,
                    primaryContent = {
                        Text(text = state.name, style = MaterialTheme.typography.headlineLarge)
                        Text(text = state.subline(), style = MaterialTheme.typography.labelLarge)
                    },
                    modifier = modifier,
                )
            }

            is MapViewModel.BottomSheetState.SingleStall -> {
                val mapEntity = state.fullMapEntity
                SheetContent(
                    onPrimaryContentHeightChange = onPrimaryContentHeightChange,
                    primaryContent = {
                        Text(text = mapEntity.name, style = MaterialTheme.typography.headlineLarge)
                        mapEntity.subline()?.let {
                            Text(text = it, style = MaterialTheme.typography.labelLarge)
                        }
                    },
                    secondaryContent = mapEntity.description?.let {
                        { Text(it) }
                    },
                    modifier = modifier,
                )
            }
        }
    }

}

@Composable
private fun SheetContent(
    onPrimaryContentHeightChange: (Dp) -> Unit,
    primaryContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    secondaryContent: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        val density = LocalDensity.current
        Column(
            verticalArrangement = Arrangement.Absolute.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    with(density) {
                        onPrimaryContentHeightChange(
                            it.size.height.toDp() + if (secondaryContent != null) 64.dp else 0.dp
                        )
                    }
                }
        ) {
            primaryContent()
            Spacer(modifier = Modifier.height(16.dp))
        }
        secondaryContent?.invoke(this@Column)
    }
}