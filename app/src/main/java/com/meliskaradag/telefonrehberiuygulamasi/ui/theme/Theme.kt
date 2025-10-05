package com.meliskaradag.telefonrehberiuygulamasi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

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

private val AppShapes = Shapes(
    small  = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp), //list kartları
    large  = RoundedCornerShape(20.dp)  //sayfa içindeki büyük paneller (Figma’da yuvarlak köşeler)
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