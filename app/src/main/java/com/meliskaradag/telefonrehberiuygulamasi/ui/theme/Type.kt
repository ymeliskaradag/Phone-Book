package com.meliskaradag.telefonrehberiuygulamasi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.em
import androidx.compose.ui.text.font.Font
import com.meliskaradag.telefonrehberiuygulamasi.R

val Mulish = FontFamily(
    Font(R.font.mulish_regular,  FontWeight.Normal),
    Font(R.font.mulish_medium,   FontWeight.Medium),
    Font(R.font.mulish_semibold, FontWeight.SemiBold),
    Font(R.font.mulish_bold,     FontWeight.Bold),
    Font(R.font.mulish_extrabold,FontWeight.ExtraBold)
)
val Onest = FontFamily(
    Font(R.font.onest_regular, FontWeight.Normal)
)

val AppTypography = Typography(

    displayLarge = TextStyle(
        fontFamily = Onest,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        lineHeight = 60.sp,
        //-2% letter spacing -> -0.02em
        letterSpacing = (-0.02).em,
        color = Gray900
    ),
    //liste/bölüm başlıkları
    headlineLarge = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        color = Gray950
    ),
    headlineMedium = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        color = Gray950
    ),
    headlineSmall = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = Gray950
    ),
    //uygulama başlıkları, kart başlıkları
    titleLarge = TextStyle(      //AppBar/ekran başlığı
        fontFamily = Mulish,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = Gray950
    ),
    titleMedium = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = Gray950
    ),
    titleSmall = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = Gray950
    ),

    //body
    bodyLarge = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = Gray500
    ),
    bodyMedium = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        color = Gray950
    ),
    bodySmall = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = Gray950
    ),

    //buton
    labelLarge = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        color = Gray950
    ),
    labelMedium = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        color = Gray950
    ),
    labelSmall = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 12.sp,
        color = Gray950
    )
)