package com.mrh.recetario.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Warm terracotta / sage palette
private val LightColors = lightColorScheme(
    primary = Color(0xFFC45D3E),           // terracotta
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDBCF),   // peach light
    onPrimaryContainer = Color(0xFF3A0B00),
    secondary = Color(0xFF6B7F5A),          // sage green
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD6EDBC), // mint cream
    onSecondaryContainer = Color(0xFF1A2E0A),
    tertiary = Color(0xFF8B6B4A),           // warm brown
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDCC2),
    onTertiaryContainer = Color(0xFF2E1500),
    background = Color(0xFFFFFBF8),         // warm white
    onBackground = Color(0xFF201A17),
    surface = Color(0xFFFFFBF8),
    onSurface = Color(0xFF201A17),
    surfaceVariant = Color(0xFFF5DED3),     // warm beige
    onSurfaceVariant = Color(0xFF52443C),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFF85746B),
    outlineVariant = Color(0xFFD8C2B8),
    surfaceTint = Color(0xFFC45D3E),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB59C),            // soft peach
    onPrimary = Color(0xFF5E1700),
    primaryContainer = Color(0xFF832600),   // deep terracotta
    onPrimaryContainer = Color(0xFFFFDBCF),
    secondary = Color(0xFFBBD1A2),          // soft sage
    onSecondary = Color(0xFF283F15),
    secondaryContainer = Color(0xFF3E5629), // forest green
    onSecondaryContainer = Color(0xFFD6EDBC),
    tertiary = Color(0xFFF0BC94),           // warm sand
    onTertiary = Color(0xFF462A0F),
    tertiaryContainer = Color(0xFF5F3E22),
    onTertiaryContainer = Color(0xFFFFDCC2),
    background = Color(0xFF1A1110),         // warm black
    onBackground = Color(0xFFF1DFDA),
    surface = Color(0xFF1A1110),
    onSurface = Color(0xFFF1DFDA),
    surfaceVariant = Color(0xFF52443C),
    onSurfaceVariant = Color(0xFFD8C2B8),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFFA08D83),
    outlineVariant = Color(0xFF52443C),
    surfaceTint = Color(0xFFFFB59C),
)

@Composable
actual fun appColorScheme(darkTheme: Boolean): ColorScheme {
    return if (darkTheme) DarkColors else LightColors
}
