package com.jonasgerdes.stoppelmap.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListLineHeader(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Line(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.size(16.dp))
        content()
        Spacer(modifier = Modifier.size(16.dp))
        Line(modifier = Modifier.weight(1f))
    }
}

@Composable
internal fun Line(modifier: Modifier = Modifier) {
    val color = LocalContentColor.current
    Box(
        modifier
            .height(1.dp)
            .background(color = color)
    )
}
