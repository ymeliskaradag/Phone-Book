package com.meliskaradag.telefonrehberiuygulamasi.core.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.meliskaradag.telefonrehberiuygulamasi.BuildConfig
import java.io.File

fun createImageUri(context: Context): Uri {
    val imagesDir = File(context.cacheDir, "images").apply { mkdirs() }
    val file = File(imagesDir, "camera_${System.currentTimeMillis()}.jpg")
    // Manifest'teki authority ile URI üret
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        file
    )
}