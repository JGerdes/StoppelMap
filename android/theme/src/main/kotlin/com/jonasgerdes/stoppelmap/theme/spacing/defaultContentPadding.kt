package com.jonasgerdes.stoppelmap.theme.spacing

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun defaultContentPadding(paddingValues: PaddingValues) =
    PaddingValues(
        top = 16.dp + paddingValues.calculateTopPadding(),
        bottom = paddingValues.calculateBottomPadding(),
        start = 16.dp,
        end = 16.dp
    )
