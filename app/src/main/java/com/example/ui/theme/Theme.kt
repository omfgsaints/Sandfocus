package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WhiteSandColorScheme = lightColorScheme(
    primary = ZenSlateAccent,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF3F4F6),
    onPrimaryContainer = ZenTextPrimary,
    secondary = ZenSageAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8F3EC),
    onSecondaryContainer = Color(0xFF1B432C),
    tertiary = ZenGoldAccent,
    onTertiary = Color.White,
    background = TableOffWhite,
    onBackground = ZenTextPrimary,
    surface = PureWhite,
    onSurface = ZenTextPrimary,
    surfaceVariant = TableSurface,
    onSurfaceVariant = ZenTextSecondary,
    outline = TableBorderLight,
    outlineVariant = TableBorderMedium,
    error = Color(0xFFDC2626),
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WhiteSandColorScheme,
        typography = Typography,
        content = content
    )
}
