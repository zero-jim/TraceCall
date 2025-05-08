package com.exoleviathan.android.tracecall.common.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.exoleviathan.android.tracecall.common.ui.color.AppBackgroundDark
import com.exoleviathan.android.tracecall.common.ui.color.AppBackgroundLight
import com.exoleviathan.android.tracecall.common.ui.color.OnPrimaryDark
import com.exoleviathan.android.tracecall.common.ui.color.OnPrimaryLight
import com.exoleviathan.android.tracecall.common.ui.color.PrimaryDark
import com.exoleviathan.android.tracecall.common.ui.color.PrimaryLight
import com.exoleviathan.android.tracecall.common.ui.color.SecondaryDark
import com.exoleviathan.android.tracecall.common.ui.color.SecondaryLight
import com.exoleviathan.android.tracecall.common.ui.color.TertiaryDark
import com.exoleviathan.android.tracecall.common.ui.color.TertiaryLight
import com.exoleviathan.android.tracecall.common.ui.type.Typography

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