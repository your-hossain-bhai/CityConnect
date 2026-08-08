package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = NaturalDarkPrimary,
    secondary = NaturalDarkSecondary,
    tertiary = NaturalDarkTertiary,
    background = NaturalDarkBackground,
    surface = NaturalDarkSurface,
    onPrimary = NaturalDarkOnPrimary,
    onSecondary = NaturalDarkOnSecondary,
    onTertiary = NaturalDarkOnTertiary,
    onBackground = NaturalDarkOnBackground,
    onSurface = NaturalDarkOnSurface,
    primaryContainer = NaturalDarkPrimaryContainer,
    onPrimaryContainer = NaturalDarkOnPrimaryContainer,
    secondaryContainer = NaturalDarkSecondaryContainer,
    onSecondaryContainer = NaturalDarkOnSecondaryContainer,
    outline = NaturalDarkOutline,
    surfaceVariant = NaturalDarkSurfaceVariant,
    onSurfaceVariant = NaturalDarkOnSurfaceVariant
  )

private val LightColorScheme =
  lightColorScheme(
    primary = NaturalLightPrimary,
    secondary = NaturalLightSecondary,
    tertiary = NaturalLightTertiary,
    background = NaturalLightBackground,
    surface = NaturalLightSurface,
    onPrimary = NaturalLightOnPrimary,
    onSecondary = NaturalLightOnSecondary,
    onTertiary = NaturalLightOnTertiary,
    onBackground = NaturalLightOnBackground,
    onSurface = NaturalLightOnSurface,
    primaryContainer = NaturalLightPrimaryContainer,
    onPrimaryContainer = NaturalLightOnPrimaryContainer,
    secondaryContainer = NaturalLightSecondaryContainer,
    onSecondaryContainer = NaturalLightOnSecondaryContainer,
    outline = NaturalLightOutline,
    surfaceVariant = NaturalLightSurfaceVariant,
    onSurfaceVariant = NaturalLightOnSurfaceVariant
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Set to false by default to strictly enforce the "Natural Tones" palette
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
