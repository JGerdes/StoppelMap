package com.jonasgerdes.stoppelmap.theme.navigation

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

class NavigationTab(
    @StringRes val label: Int,
    val startDestination: Any,
    val iconComposable: @Composable () -> Unit,
) {
    constructor(
        @StringRes label: Int,
        startDestination: Any,
        icon: ImageVector,
    ) : this(label, startDestination, {
        Icon(icon, contentDescription = null)
    })
}