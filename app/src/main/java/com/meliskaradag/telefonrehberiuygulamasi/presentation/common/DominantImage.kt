package com.meliskaradag.telefonrehberiuygulamasi.presentation.common

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest

//Baskın renge göre arkaplanın değişmesi için
@Composable
fun DominantShadowImage(url: String, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    var bg by remember { mutableStateOf(Color(0xFFEDEDED)) }

    //URL değişince bir kez çalışması için
    LaunchedEffect(url) {
        try {
            val loader = ImageLoader(ctx)
            val req = ImageRequest.Builder(ctx).data(url).allowHardware(false).build()
            val result = loader.execute(req).drawable
            val bmp = (result as? android.graphics.drawable.BitmapDrawable)?.bitmap ?: return@LaunchedEffect
            val p = Palette.from(bmp).generate()
            bg = Color(p.getDominantColor(0xFFEDEDED.toInt()))
        } catch (_: Throwable) { /* fallback rengi kalır */ }
    }

    Surface(color = bg.copy(alpha = 0.12f), tonalElevation = 6.dp) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(8.dp)
        )
    }
}