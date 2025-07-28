package com.jonasgerdes.stoppelmap.theme.material

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun appBarContainerColor(scrollBehavior: TopAppBarScrollBehavior): State<Color> {
    val colors = listOf(
        TopAppBarDefaults.topAppBarColors().containerColor,
        TopAppBarDefaults.topAppBarColors().scrolledContainerColor,
    )
    val targetColor by remember(colors, scrollBehavior) {
        derivedStateOf {
            val overlappingFraction = scrollBehavior.state.overlappedFraction
            lerp(
                colors[0],
                colors[1],
                FastOutLinearInEasing.transform(if (overlappingFraction > 0.01f) 1f else 0f)
            )
        }
    }

    return animateColorAsState(
        targetColor,
        animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec(),
    )
}