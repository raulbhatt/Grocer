package com.rahul.grocer.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

private val DarkColorScheme = darkColorScheme(
    primary = NebulaPurple,
    secondary = CosmicPink,
    tertiary = FreshGreen,
    background = DeepSpaceBlue,
    surface = DeepSpaceBlue,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = StarlightSilver,
    onSurface = StarlightSilver,
)

private val LightColorScheme = lightColorScheme(
    primary = NebulaPurple,
    secondary = CosmicPink,
    tertiary = FreshGreen,
    // For now, we force dark mode vibes even in light mode or define a proper light mode later.
    // But the prompt asked for "Deep Space Dark Mode" as a key identity.
    // Let's keep light mode standard for fallback but prioritize dark.
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun OrbitTheme(
    // darkTheme: Boolean = isSystemInDarkTheme(), // Removed to use Repository
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val isDarkTheme by com.rahul.grocer.data.ThemeRepository.isDarkTheme.collectAsState()
    
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
