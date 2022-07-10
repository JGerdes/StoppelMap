package com.jonasgerdes.stoppelmap.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

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
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme)
                dynamicDarkColorScheme(context)
            else
                dynamicLightColorScheme(context).copy(
                    // The background color of the dynamic light scheme usually has a
                    // tint, that doesn't look so nice.
                    // TODO: Use TonalPalette.neutral100 instead
                    background = LightColorScheme.background
                )
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}
