package ru.alexey.ndimmatrix.generator.uikit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// File: Theme.kt

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF607D8B),          // Blue Gray 500
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCFD8DC), // Blue Gray 100

    secondary = Color(0xFF9575CD),        // Soft Purple
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1C4E9),

    background = Color(0xFFF5F5F5),       // Soft Gray
    onBackground = Color(0xFF212121),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF333333),

    error = Color(0xFFE57373),            // Soft Red
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90A4AE),          // Blue Gray 300
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF455A64),

    secondary = Color(0xFFB39DDB),        // Soft Purple
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF5E35B1),

    background = Color(0xFF303030),       // Dark Gray
    onBackground = Color(0xFFE0E0E0),

    surface = Color(0xFF424242),
    onSurface = Color(0xFFE0E0E0),

    error = Color(0xFFEF9A9A),            // Muted Red
    onError = Color.Black
)

@Composable
fun ConfigTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
