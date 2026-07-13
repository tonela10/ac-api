package com.example.acapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object AcColors {
    val Sand = Color(0xFFFBF6E4)
    val Paper = Color(0xFFF4E9C1)
    val Mint = Color(0xFF6FBFA8)
    val MintDeep = Color(0xFF3E8F7A)
    val Coral = Color(0xFFEF8A7F)
    val Leaf = Color(0xFF3E4B2E)
    val LeafSoft = Color(0xFF6B7A5A)
    val Sky = Color(0xFFA9D5E5)
    val Sun = Color(0xFFF6C453)
}

private fun acLightColorScheme(): ColorScheme = lightColorScheme(
    primary = AcColors.Mint,
    onPrimary = Color.White,
    primaryContainer = AcColors.Mint.copy(alpha = 0.25f),
    onPrimaryContainer = AcColors.Leaf,

    secondary = AcColors.Coral,
    onSecondary = Color.White,
    secondaryContainer = AcColors.Sky,
    onSecondaryContainer = AcColors.Leaf,

    tertiary = AcColors.Sun,
    onTertiary = AcColors.Leaf,

    background = AcColors.Sand,
    onBackground = AcColors.Leaf,
    surface = AcColors.Paper,
    onSurface = AcColors.Leaf,
    surfaceVariant = AcColors.Paper,
    onSurfaceVariant = AcColors.LeafSoft,

    error = AcColors.Coral,
    onError = Color.White,

    outline = AcColors.LeafSoft.copy(alpha = 0.4f),
    outlineVariant = AcColors.LeafSoft.copy(alpha = 0.2f),
)

private val AcShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp),
)

private fun acTypography(): Typography {
    val base = Typography()
    val family = FontFamily.SansSerif
    fun TextStyle.tune(weight: FontWeight, spacing: Double = 0.0) = copy(
        fontFamily = family,
        fontWeight = weight,
        letterSpacing = spacing.sp,
    )
    return base.copy(
        displayLarge = base.displayLarge.tune(FontWeight.ExtraBold, 0.2),
        displayMedium = base.displayMedium.tune(FontWeight.ExtraBold, 0.2),
        displaySmall = base.displaySmall.tune(FontWeight.ExtraBold, 0.2),
        headlineLarge = base.headlineLarge.tune(FontWeight.ExtraBold, 0.2),
        headlineMedium = base.headlineMedium.tune(FontWeight.ExtraBold, 0.2),
        headlineSmall = base.headlineSmall.tune(FontWeight.ExtraBold, 0.2),
        titleLarge = base.titleLarge.tune(FontWeight.Bold, 0.1),
        titleMedium = base.titleMedium.tune(FontWeight.Bold, 0.1),
        titleSmall = base.titleSmall.tune(FontWeight.SemiBold, 0.1),
        bodyLarge = base.bodyLarge.tune(FontWeight.Normal),
        bodyMedium = base.bodyMedium.tune(FontWeight.Normal),
        bodySmall = base.bodySmall.tune(FontWeight.Normal),
        labelLarge = base.labelLarge.tune(FontWeight.SemiBold, 0.4),
        labelMedium = base.labelMedium.tune(FontWeight.SemiBold, 0.4),
        labelSmall = base.labelSmall.tune(FontWeight.SemiBold, 0.6),
    )
}

@Composable
fun AcTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = acLightColorScheme(),
        typography = acTypography(),
        shapes = AcShapes,
        content = content,
    )
}
