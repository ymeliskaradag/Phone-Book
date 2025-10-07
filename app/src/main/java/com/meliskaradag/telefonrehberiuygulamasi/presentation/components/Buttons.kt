package com.meliskaradag.telefonrehberiuygulamasi.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.meliskaradag.telefonrehberiuygulamasi.ui.theme.*
import androidx.compose.ui.graphics.Color

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue, contentColor = Color.White),
        modifier = modifier.height(48.dp)
    ) { Text(text) }
}

@Composable
fun DangerButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = ErrorRed, contentColor = Color.White),
        modifier = modifier.height(48.dp)
    ) { Text(text) }
}

@Composable
fun OutlineNeutralButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = if (enabled) Gray900 else Gray300
        ),
        border = BorderStroke(1.dp, if (enabled) Gray300 else Gray200),
        modifier = modifier.height(48.dp)
    ) { Text(text) }
}
