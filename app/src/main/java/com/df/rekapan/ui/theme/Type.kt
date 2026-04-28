package com.df.rekapan.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.df.rekapan.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs
)

/** Used for the app wordmark ("rekap.") and primary headings. */
val SoraFamily = FontFamily(
    Font(googleFont = GoogleFont("Sora"), fontProvider = provider, weight = FontWeight.ExtraBold),
    Font(googleFont = GoogleFont("Sora"), fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = GoogleFont("Sora"), fontProvider = provider, weight = FontWeight.Normal),
)

/** Used for body text, greetings, and date labels. */
val RubikFamily = FontFamily(
    Font(googleFont = GoogleFont("Rubik"), fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = GoogleFont("Rubik"), fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = GoogleFont("Rubik"), fontProvider = provider, weight = FontWeight.Bold),
)

/** Used for UI controls such as the period toggle tabs. */
val PoppinsFamily = FontFamily(
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.Bold),
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = RubikFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = RubikFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = RubikFamily,
        fontWeight = FontWeight.Bold,
        fontSize   = 22.sp,
        lineHeight = 28.sp,
    ),
)