package com.jonasgerdes.stoppelmap.theme.util

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api = 31)
fun Int.supportsDynamicColor() = Build.VERSION.SDK_INT >= 31

@ChecksSdkIntAtLeast(api = 29)
fun Int.supportsDarkTheme() = Build.VERSION.SDK_INT >= 29

@ChecksSdkIntAtLeast(api = 27)
fun Int.supportsLightStatusBarAppearance() = Build.VERSION.SDK_INT >= 27

@ChecksSdkIntAtLeast(api = 23)
fun Int.supportsLightNavigationBarAppearance() = Build.VERSION.SDK_INT >= 23
