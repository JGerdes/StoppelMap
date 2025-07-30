package com.jonasgerdes.stoppelmap.map.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@Composable
fun SearchLoadingIndicator(
    inProgress: Boolean,
    modifier: Modifier = Modifier,
) {
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
            modifier = modifier.fillMaxWidth()
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