package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val SynkoLightColorScheme = lightColorScheme(
    primary = SynkoPrimary,
    onPrimary = Color.White,
    primaryContainer = SynkoAccentLight,
    onPrimaryContainer = SynkoPrimary,
    secondary = SynkoAccent,
    onSecondary = Color.White,
    secondaryContainer = SynkoSurfaceVariant,
    onSecondaryContainer = SynkoTextPrimary,
    tertiary = SynkoSuccess,
    onTertiary = Color.White,
    background = SynkoWhite,
    onBackground = SynkoTextPrimary,
    surface = SynkoWhite,
    onSurface = SynkoTextPrimary,
    surfaceVariant = SynkoSurface,
    onSurfaceVariant = SynkoTextSecondary,
    outline = SynkoBorder,
    error = SynkoError,
    onError = Color.White,
    errorContainer = SynkoErrorLight,
    onErrorContainer = SynkoError
)

val SynkoShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun SynkoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SynkoLightColorScheme,
        typography = Typography,
        shapes = SynkoShapes,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    SynkoTheme(content = content)
}

