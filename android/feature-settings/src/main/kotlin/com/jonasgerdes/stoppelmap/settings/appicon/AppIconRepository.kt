package com.jonasgerdes.stoppelmap.settings.appicon

import androidx.annotation.DrawableRes
import kotlinx.coroutines.flow.StateFlow

interface AppIconRepository {
    val appIcons: StateFlow<List<AppIcon>>
    fun setIcon(newIcon: AppIcon)
    fun updateAppIcons()
}

data class AppIcon(
    @field:DrawableRes val drawable: Int,
    val selected: Boolean,
)