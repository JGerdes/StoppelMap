package com.jonasgerdes.stoppelmap.theme.modifier

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun LazyListState.isAtTop() = remember {
    derivedStateOf {
        firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0
    }
}

fun Modifier.elevationWhenScrolled(
    lazyListState: LazyListState,
    elevation: Dp = 8.dp
): Modifier = composed {

    val isAtTop by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0
                    && lazyListState.firstVisibleItemScrollOffset == 0
        }
    }
    Log.d("elevationWhenScrolled", "isAtTop changed: $isAtTop")
    val effectiveElevation by animateDpAsState(targetValue = if (isAtTop) 0.dp else elevation)

    shadow(elevation = effectiveElevation).zIndex(1f)
}

fun Modifier.elevationWhenScrolled(
    lazyGridState: LazyGridState,
    elevation: Dp = 8.dp
): Modifier = composed {

    val isAtTop by remember {
        derivedStateOf {
            lazyGridState.firstVisibleItemIndex == 0
                    && lazyGridState.firstVisibleItemScrollOffset == 0
        }
    }
    val effectiveElevation by animateDpAsState(targetValue = if (isAtTop) 0.dp else elevation)

    shadow(elevation = effectiveElevation)
}
