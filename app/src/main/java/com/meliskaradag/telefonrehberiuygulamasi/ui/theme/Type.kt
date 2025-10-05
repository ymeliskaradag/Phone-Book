package com.meliskaradag.telefonrehberiuygulamasi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font

//Google Fonts: Mulish & Onest
private val provider = GoogleFont.Provider("com.google.android.gms.fonts", "com.google.android.gms")
private val Mulish = FontFamily(Font(GoogleFont("Mulish"), provider))
private val Onest  = FontFamily(Font(GoogleFont("Onest"),  provider))

// Figma notlarına göre örnek roller (fotoğraftaki "Title 14", "Body 12" vb.)

val AppTypography = Typography(
    titleLarge = TextStyle( //"Contacts" başlığı
        fontFamily = Mulish,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = Gray500
    ),
    titleMedium = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = Gray500
    ),
    bodyLarge = TextStyle( //liste isimleri
        fontFamily = Mulish,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        color = Gray950
    ),
    bodyMedium = TextStyle( //telefon numarası vb.
        fontFamily = Mulish,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp, lineHeight = 16.sp,
        color = Gray950
    ),
    labelMedium = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = Gray500
    ),
    bodySmall = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp, lineHeight = 16.sp,
        color = Gray950
    )
)