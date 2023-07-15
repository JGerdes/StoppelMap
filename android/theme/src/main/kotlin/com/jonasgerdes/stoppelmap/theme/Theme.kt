package com.jonasgerdes.stoppelmap.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = StoppelPurpleBrightened,
    secondary = StoppelIndigoBrightened,
    tertiary = StoppelPinkBrightened,
)

private val LightColorScheme = lightColorScheme(
    primary = StoppelPurple,
    secondary = StoppelIndigo,
    tertiary = StoppelPink,
)

@Composable
fun StoppelMapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme)
                dynamicDarkColorScheme(context)
            else
                dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    val fallbackSystemBarColor = MaterialTheme.colorScheme.primary.toArgb()
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            window.navigationBarColor =
                if (Build.VERSION.SDK_INT >= 27 || darkTheme) Color.Transparent.toArgb()
                else fallbackSystemBarColor

            window.statusBarColor =
                if (Build.VERSION.SDK_INT >= 23 || darkTheme) Color.Transparent.toArgb()
                else fallbackSystemBarColor

            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}

val ColorScheme.onScrim get() = Color.White
