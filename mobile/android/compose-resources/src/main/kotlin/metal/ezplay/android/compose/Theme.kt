package metal.ezplay.android.compose

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = darkColors(
  primary = DarkPrimary,
  secondary = DarkSecondary,
  background = DarkSecondary,
  surface = DarkPrimary,
  onSecondary = DarkPrimary,
  onBackground = DarkPrimary,
)

private val DarkColorScheme = darkColorScheme(
  primary = DarkPrimary,
  secondary = DarkSecondary
)

private val LightColorPalette = lightColors(
  primary = LightPrimary,
  secondary = LightSecondary,
  background = LightSecondary,
  surface = LightPrimary,
  onSecondary = LightPrimary,
  onBackground = LightPrimary,
)

private val LightColorScheme = darkColorScheme(
  primary = LightPrimary,
  secondary = LightSecondary
)

@Composable
fun AppTheme(
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colors = when {
    useDarkTheme -> DarkColorPalette
    else -> LightColorPalette
  }
  val colorScheme = when {
    useDarkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = colors.secondary.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = useDarkTheme
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    content = content
  )
}
