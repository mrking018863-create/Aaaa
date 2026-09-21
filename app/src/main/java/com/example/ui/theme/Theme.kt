package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SoloColorScheme = darkColorScheme(
  primary = SoloElectricBlue,
  onPrimary = SoloObsidian,
  primaryContainer = SoloHunterBlue,
  onPrimaryContainer = SoloTextPrimary,
  secondary = SoloMonarchPurple,
  onSecondary = SoloObsidian,
  secondaryContainer = SoloSurfaceVariant,
  onSecondaryContainer = SoloTextPrimary,
  tertiary = SoloCyanGlow,
  onTertiary = SoloObsidian,
  background = SoloObsidian,
  onBackground = SoloTextPrimary,
  surface = SoloSurface,
  onSurface = SoloTextPrimary,
  surfaceVariant = SoloSurfaceVariant,
  onSurfaceVariant = SoloTextSecondary,
  error = SoloPenaltyRed,
  onError = SoloTextPrimary,
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = SoloColorScheme,
    typography = Typography,
    content = content
  )
}

