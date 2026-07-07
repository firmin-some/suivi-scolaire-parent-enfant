package bf.ujkz.suiviscolaireparent.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = SchoolBlue,
    onPrimary = Color.White,
    secondary = SchoolGold,
    onSecondary = Color.White,
    background = SchoolBackground,      // fond gris clair
    surface = SchoolSurface,            // cartes blanches
    onBackground = Color(0xFF1A1A2E),   // texte foncé sur fond clair
    onSurface = Color(0xFF1A1A2E),      // texte foncé sur cartes
    surfaceVariant = Color(0xFFE8EDF2),
    onSurfaceVariant = Color(0xFF1A1A2E)
)

private val DarkColorScheme = darkColorScheme(
    primary = SchoolBlueLight,
    onPrimary = Color.White,
    secondary = SchoolGold,
    onSecondary = Color.White
)

@Composable
fun SuiviScolaireParentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}