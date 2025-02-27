package dev.yarobot.shirmaz.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import org.jetbrains.compose.resources.Font
import shirmaz.shirmazapp.generated.resources.Res
import shirmaz.shirmazapp.generated.resources.mulish_regular
import shirmaz.shirmazapp.generated.resources.mulish_semibold

private val baseline = Typography()

private val shirmazFontFamily
    @Composable
    get() = FontFamily(
        Font(
            resource = Res.font.mulish_regular,
            weight = W400
        ),
        Font(
            resource = Res.font.mulish_semibold,
            weight = W600
        )
    )

internal val shirmazTypography
    @Composable
    get() = Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = shirmazFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = shirmazFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = shirmazFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = shirmazFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = shirmazFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = shirmazFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = shirmazFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = shirmazFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = shirmazFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = shirmazFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = shirmazFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = shirmazFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = shirmazFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = shirmazFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = shirmazFontFamily, fontWeight = W600),
    )