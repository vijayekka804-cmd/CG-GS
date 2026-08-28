package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = CgBluePrimaryLight,
    onPrimary = Color.White,
    primaryContainer = CgBluePrimaryDark,
    onPrimaryContainer = Color.White,
    secondary = CgGoldAccent,
    onSecondary = Color.Black,
    secondaryContainer = CgGoldAccentDark,
    onSecondaryContainer = Color.White,
    tertiary = CgSecondary,
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFF334155),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
    outline = Color(0xFF475569)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = CgBluePrimary,
    onPrimary = Color.White,
    primaryContainer = CgBluePrimaryContainer,
    onPrimaryContainer = CgBlueOnPrimaryContainer,
    secondary = CgGoldAccent,
    onSecondary = Color.White,
    secondaryContainer = CgGoldAccentLight,
    onSecondaryContainer = CgGoldAccentDark,
    tertiary = CgSecondary,
    background = CgBackground,
    surface = CgSurface,
    surfaceVariant = CgSurfaceVariant,
    onBackground = CgTextPrimary,
    onSurface = CgTextPrimary,
    onSurfaceVariant = CgTextSecondary,
    outline = CgBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Keep the explicit educational brand colors consistent
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

