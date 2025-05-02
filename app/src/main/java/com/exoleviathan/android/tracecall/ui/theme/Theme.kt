package com.exoleviathan.android.tracecall.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    background = AppBackgroundLight,
    surface = AppBackgroundLight,
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    secondary = SecondaryLight,
    tertiary = TertiaryLight
)

private val DarkColorScheme = darkColorScheme(
    background = AppBackgroundDark,
    surface = AppBackgroundDark,
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    secondary = SecondaryDark,
    tertiary = TertiaryDark
)

@Composable
fun TraceCallTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme

        else -> LightColorScheme
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}