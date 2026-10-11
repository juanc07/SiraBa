package net.thinkbloxph.siraba.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp

/** Sira Ba? brand tokens. Keep the same distinct brand in both system appearances. */
object SiraBaColors {
    val Teal = Color(0xFF087F81)
    val TealDark = Color(0xFF065E63)
    val Ink = Color(0xFF122D38)
    val Paper = Color(0xFFF8F8F4)
    val Mint = Color(0xFFE4F4EF)
    val Slate = Color(0xFF5A7077)
    val Gold = Color(0xFFFFD48D)
    val DeepCard = Color(0xFF123E47)
}

private val LightPalette = lightColorScheme(
    primary = SiraBaColors.TealDark,
    onPrimary = Color.White,
    primaryContainer = SiraBaColors.Mint,
    onPrimaryContainer = SiraBaColors.Ink,
    secondary = SiraBaColors.Teal,
    onSecondary = Color.White,
    background = SiraBaColors.Paper,
    onBackground = SiraBaColors.Ink,
    surface = Color.White,
    onSurface = SiraBaColors.Ink,
    surfaceVariant = Color(0xFFEAF0ED),
    onSurfaceVariant = SiraBaColors.Slate,
    outline = Color(0xFFB8C7C7)
)

private val DarkPalette = darkColorScheme(
    primary = Color(0xFF75D7CE),
    onPrimary = Color(0xFF00383C),
    primaryContainer = Color(0xFF174A4E),
    onPrimaryContainer = Color(0xFFDCF8F0),
    secondary = Color(0xFF9DD7C8),
    onSecondary = Color(0xFF15392F),
    background = Color(0xFF0B2028),
    onBackground = Color(0xFFE8F4F3),
    surface = Color(0xFF18313A),
    onSurface = Color(0xFFE8F4F3),
    surfaceVariant = Color(0xFF28434A),
    onSurfaceVariant = Color(0xFFBDCECD),
    outline = Color(0xFF607A7D)
)

private val SiraBaTypography = Typography(
    headlineLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 38.sp, lineHeight = 44.sp, letterSpacing = (-1.4).sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 29.sp, lineHeight = 35.sp, letterSpacing = (-0.6).sp),
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 24.sp),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 25.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 22.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 20.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 17.sp)
)

private val SiraBaShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun SiraBaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkPalette else LightPalette,
        typography = SiraBaTypography,
        shapes = SiraBaShapes,
        content = content
    )
}
