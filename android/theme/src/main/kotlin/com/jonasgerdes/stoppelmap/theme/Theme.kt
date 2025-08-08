package com.jonasgerdes.stoppelmap.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.jonasgerdes.stoppelmap.theme.settings.ColorSchemeSetting
import com.jonasgerdes.stoppelmap.theme.settings.ThemeSetting
import com.jonasgerdes.stoppelmap.theme.util.supportsDynamicColor
import com.jonasgerdes.stoppelmap.theme.util.supportsLightNavigationBarAppearance
import com.jonasgerdes.stoppelmap.theme.util.supportsLightStatusBarAppearance

val DarkColorScheme = darkColorScheme(
    primary = StoppelIndigoBrightened,
    secondary = StoppelIndigoBrightened,
    tertiary = StoppelPinkBrightened,
)

val LightColorScheme = lightColorScheme(
    primary = StoppelIndigo,
    secondary = StoppelIndigo,
    tertiary = StoppelPink,
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun StoppelMapTheme(
    themeSetting: ThemeSetting = ThemeSetting.Light,
    colorSchemeSetting: ColorSchemeSetting = ColorSchemeSetting.Classic,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalThemeSetting provides themeSetting) {
        val darkTheme = LocalThemeSetting.current.isDarkTheme()

        val colorScheme = when {
            colorSchemeSetting == ColorSchemeSetting.System &&
                    Build.VERSION.SDK_INT.supportsDynamicColor() -> {
                val context = LocalContext.current
                if (darkTheme)
                    dynamicDarkColorScheme(context)
                else
                    dynamicLightColorScheme(context)
            }

            colorSchemeSetting == ColorSchemeSetting.Classic -> if (darkTheme) DarkColorScheme else LightColorScheme
            else -> if (darkTheme) DarkColorScheme else LightColorScheme
        }

        val view = LocalView.current
        val fallbackSystemBarColor = MaterialTheme.colorScheme.primary.toArgb()
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                val insetsController = WindowCompat.getInsetsController(window, view)

                window.navigationBarColor =
                    if (Build.VERSION.SDK_INT.supportsLightNavigationBarAppearance() || darkTheme)
                        Color.Transparent.toArgb()
                    else fallbackSystemBarColor

                window.statusBarColor =
                    if (Build.VERSION.SDK_INT.supportsLightStatusBarAppearance() || darkTheme)
                        Color.Transparent.toArgb()
                    else fallbackSystemBarColor

                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            typography = Typography,
            motionScheme = MotionScheme.expressive(),
            content = content
        )
    }
}

val ColorScheme.onScrim get() = Color.White

val LocalThemeSetting = compositionLocalOf { ThemeSetting.default }

@Composable
fun ThemeSetting.isDarkTheme() = when (this) {
    ThemeSetting.Light -> false
    ThemeSetting.Dark -> true
    ThemeSetting.System -> isSystemInDarkTheme()
}
