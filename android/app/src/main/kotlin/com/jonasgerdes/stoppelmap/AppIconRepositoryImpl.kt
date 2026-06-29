package com.jonasgerdes.stoppelmap

import android.content.ComponentName
import android.content.pm.PackageManager
import androidx.annotation.DrawableRes
import com.jonasgerdes.stoppelmap.settings.appicon.AppIcon
import com.jonasgerdes.stoppelmap.settings.appicon.AppIconRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppIconRepositoryImpl(
    private val packageManager: PackageManager
) : AppIconRepository {

    private val _appIcons = MutableStateFlow<List<AppIcon>>(emptyList())

    override val appIcons: StateFlow<List<AppIcon>> = _appIcons

    data class AvailableAppIcon(
        @field:DrawableRes val drawable: Int,
        val component: ComponentName,
        val enabledState: Int = PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        val disabledState: Int = PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
    )

    private val availableAppIcons = listOf(
        AvailableAppIcon(
            R.drawable.ic_launcher,
            component = ComponentName(
                BuildConfig.APPLICATION_ID,
                "${BuildConfig.APPLICATION_ID.removeSuffix(".debug")}.Launcher"
            ),
            enabledState = PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
        ),
        AvailableAppIcon(
            R.drawable.ic_launcher_swing_ride,
            component = ComponentName(
                BuildConfig.APPLICATION_ID,
                "${BuildConfig.APPLICATION_ID.removeSuffix(".debug")}.LauncherSwingRide"
            ),
        ),
        AvailableAppIcon(
            R.drawable.ic_launcher_pride,
            component = ComponentName(
                BuildConfig.APPLICATION_ID,
                "${BuildConfig.APPLICATION_ID.removeSuffix(".debug")}.LauncherPride"
            ),
        ),
        AvailableAppIcon(
            drawable = R.drawable.ic_launcher_pride_v2,
            component = ComponentName(
                BuildConfig.APPLICATION_ID,
                "${BuildConfig.APPLICATION_ID.removeSuffix(".debug")}.LauncherPrideV2"
            ),
        ),
    )

    override fun updateAppIcons() {
        _appIcons.value = availableAppIcons.map { icon ->
            AppIcon(
                drawable = icon.drawable,
                selected = packageManager.getComponentEnabledSetting(icon.component) == icon.enabledState,
            )
        }
    }


    override fun setIcon(newIcon: AppIcon) {
        availableAppIcons.forEach { availableIcon ->
            val state = if (availableIcon.drawable == newIcon.drawable) {
                availableIcon.enabledState
            } else {
                availableIcon.disabledState
            }

            packageManager.setComponentEnabledSetting(
                availableIcon.component,
                state,
                PackageManager.DONT_KILL_APP
            )
        }
        updateAppIcons()
    }

}