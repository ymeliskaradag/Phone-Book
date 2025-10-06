package com.meliskaradag.telefonrehberiuygulamasi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    secondary = Gray500,
    onSecondary = Color.White,
    background = Background,
    onBackground = OnBackground,
    surface = SurfaceColor,
    onSurface = OnSurface,
    outline = Outline,
    error = ErrorRed
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}