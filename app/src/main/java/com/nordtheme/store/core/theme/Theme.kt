package com.nordtheme.store.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class NordPalette(
    val background: Color,
    val surfaceCard: Color,
    val surfacePill: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val coralCta: Color,
    val badgeRed: Color,
    val glassTop: Color,
    val glassBottom: Color,
    val glassBorder: Color,
    val isDark: Boolean
)

val DarkNordPalette = NordPalette(
    background = Color(0xFF000000),
    surfaceCard = Color(0xFF161618),
    surfacePill = Color(0xFF1E1E20),
    textPrimary = Color(0xFFFFFFFF),
    textMuted = Color(0xFF8E8E93),
    coralCta = Color(0xFFEE3B33),
    badgeRed = Color(0xFFFF3B30),
    glassTop = Color(0xCC2A2A2E),
    glassBottom = Color(0xE61A1A1D),
    glassBorder = Color(0x33FFFFFF),
    isDark = true
)

val LightNordPalette = NordPalette(
    background = Color(0xFFF6F6F9),
    surfaceCard = Color(0xFFFFFFFF),
    surfacePill = Color(0xFFEBEBF0),
    textPrimary = Color(0xFF111114),
    textMuted = Color(0xFF6E6E73),
    coralCta = Color(0xFFEE3B33),
    badgeRed = Color(0xFFFF3B30),
    glassTop = Color(0xE6FFFFFF),
    glassBottom = Color(0xCCF2F2F7),
    glassBorder = Color(0x22000000),
    isDark = false
)

val LocalNordPalette = staticCompositionLocalOf { DarkNordPalette }

@Composable
fun NordThemeStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val palette = if (darkTheme) DarkNordPalette else LightNordPalette
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = palette.coralCta,
            background = palette.background,
            surface = palette.surfaceCard,
            onBackground = palette.textPrimary,
            onSurface = palette.textPrimary
        )
    } else {
        lightColorScheme(
            primary = palette.coralCta,
            background = palette.background,
            surface = palette.surfaceCard,
            onBackground = palette.textPrimary,
            onSurface = palette.textPrimary
        )
    }

    CompositionLocalProvider(LocalNordPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
