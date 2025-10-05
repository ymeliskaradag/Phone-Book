package com.meliskaradag.telefonrehberiuygulamasi.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// TODO: Figma renk tokenlarını buraya taşı (Primary/Secondary/Background/Surface vb.)
private val LightColors: ColorScheme = lightColorScheme(
    primary = Color(0xFF006E4F),    // FIGMA_PRIMARY
    onPrimary = Color.White,
    secondary = Color(0xFF4B635A),  // FIGMA_SECONDARY
    onSecondary = Color.White,
    background = Color(0xFFF8FAF9),
    onBackground = Color(0xFF1B1B1B),
    surface = Color.White,
    onSurface = Color(0xFF1B1B1B),
    error = Color(0xFFB3261E),
    onError = Color.White
)
private val DarkColors: ColorScheme = darkColorScheme(
    primary = Color(0xFF51D1AE),
    onPrimary = Color(0xFF003828),
    secondary = Color(0xFFB3CCC3),
    onSecondary = Color(0xFF1A251F),
    background = Color(0xFF111311),
    onBackground = Color(0xFFE4E4E4),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFE4E4E4),
    error = Color(0xFFFFB4A9),
    onError = Color(0xFF680003)
)

// TODO: Figma tipografi tokenlarını buraya taşı (H1/H2/Title/Body/Label vb.)
private val AppTypography = Typography()

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}