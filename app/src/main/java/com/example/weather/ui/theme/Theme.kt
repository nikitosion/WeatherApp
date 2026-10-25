package com.example.weather.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Periwinkle,
    onPrimary = DarkWeatherColors.OnPrimary,
    primaryContainer = DarkWeatherColors.PrimaryContainer,
    onPrimaryContainer = DarkWeatherColors.OnPrimaryContainer,
    inversePrimary = LightWeatherColors.Primary,
    secondary = DarkWeatherColors.Secondary,
    onSecondary = DarkWeatherColors.OnSecondary,
    secondaryContainer = DarkWeatherColors.SecondaryContainer,
    onSecondaryContainer = DarkWeatherColors.OnSecondaryContainer,
    tertiary = DarkWeatherColors.Tertiary,
    onTertiary = DarkWeatherColors.OnTertiary,
    tertiaryContainer = DarkWeatherColors.TertiaryContainer,
    onTertiaryContainer = DarkWeatherColors.OnTertiaryContainer,
    background = DarkWeatherColors.Background,
    onBackground = DarkWeatherColors.OnSurface,
    surface = DarkWeatherColors.Background,
    onSurface = DarkWeatherColors.OnSurface,
    surfaceVariant = DarkWeatherColors.SurfaceVariant,
    onSurfaceVariant = DarkWeatherColors.OnSurfaceVariant,
    surfaceTint = Periwinkle,
    inverseSurface = LightWeatherColors.SecondaryContainer,
    inverseOnSurface = LightWeatherColors.OnSurface,
    outline = DarkWeatherColors.Outline,
    outlineVariant = DarkWeatherColors.OutlineVariant,
    surfaceDim = DarkWeatherColors.Background,
    surfaceBright = DarkWeatherColors.SurfaceBright,
    surfaceContainerLowest = DarkWeatherColors.SurfaceContainerLowest,
    surfaceContainerLow = DarkWeatherColors.SurfaceContainerLow,
    surfaceContainer = DarkWeatherColors.SurfaceContainer,
    surfaceContainerHigh = DarkWeatherColors.SurfaceContainerHigh,
    surfaceContainerHighest = DarkWeatherColors.SurfaceContainerHighest
)

private val LightColorScheme = lightColorScheme(
    primary = LightWeatherColors.Primary,
    onPrimary = LightWeatherColors.OnPrimary,
    primaryContainer = Periwinkle,
    onPrimaryContainer = LightWeatherColors.OnPrimaryContainer,
    inversePrimary = Periwinkle,
    secondary = LightWeatherColors.Secondary,
    onSecondary = LightWeatherColors.OnSecondary,
    secondaryContainer = LightWeatherColors.SecondaryContainer,
    onSecondaryContainer = LightWeatherColors.OnSecondaryContainer,
    tertiary = LightWeatherColors.Tertiary,
    onTertiary = LightWeatherColors.OnTertiary,
    tertiaryContainer = LightWeatherColors.TertiaryContainer,
    onTertiaryContainer = LightWeatherColors.OnTertiaryContainer,
    background = LightWeatherColors.Background,
    onBackground = LightWeatherColors.OnSurface,
    surface = LightWeatherColors.Background,
    onSurface = LightWeatherColors.OnSurface,
    surfaceVariant = LightWeatherColors.SecondaryContainer,
    onSurfaceVariant = LightWeatherColors.OnSurfaceVariant,
    surfaceTint = LightWeatherColors.Primary,
    inverseSurface = DarkWeatherColors.SurfaceContainer,
    inverseOnSurface = DarkWeatherColors.OnSurface,
    outline = LightWeatherColors.Outline,
    outlineVariant = LightWeatherColors.OutlineVariant,
    surfaceDim = LightWeatherColors.SurfaceDim,
    surfaceBright = LightWeatherColors.SurfaceBright,
    surfaceContainerLowest = LightWeatherColors.SurfaceContainerLowest,
    surfaceContainerLow = LightWeatherColors.SurfaceContainerLow,
    surfaceContainer = LightWeatherColors.SurfaceContainer,
    surfaceContainerHigh = LightWeatherColors.SurfaceContainerHigh,
    surfaceContainerHighest = LightWeatherColors.SurfaceContainerHighest
)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep the Periwinkle palette unless wallpaper-based colors are explicitly requested.
    dynamicColor: Boolean = false,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
