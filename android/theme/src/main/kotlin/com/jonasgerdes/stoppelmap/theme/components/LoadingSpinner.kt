@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.jonasgerdes.stoppelmap.theme.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingSpinner(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        ContainedLoadingIndicator()
    }
}
