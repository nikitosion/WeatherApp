package com.example.weather.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.example.weather.R

@OptIn(ExperimentalTextApi::class)
val InterFontFamily = FontFamily(
    listOf(FontWeight.Normal, FontWeight.Medium, FontWeight.SemiBold).map { weight ->
        Font(
            resId = R.font.inter_variable,
            weight = weight,
            variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))
        )
    }
)

private val DefaultTypography = Typography()

val Typography = DefaultTypography.copy(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = InterFontFamily),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = InterFontFamily),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = InterFontFamily),
    headlineLarge = DefaultTypography.headlineLarge.copy(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold
    ),
    headlineMedium = DefaultTypography.headlineMedium.copy(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold
    ),
    headlineSmall = DefaultTypography.headlineSmall.copy(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold
    ),
    titleLarge = DefaultTypography.titleLarge.copy(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold
    ),
    titleMedium = DefaultTypography.titleMedium.copy(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold
    ),
    titleSmall = DefaultTypography.titleSmall.copy(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold
    ),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = InterFontFamily),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = InterFontFamily),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = InterFontFamily),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = InterFontFamily),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = InterFontFamily),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = InterFontFamily)
)
