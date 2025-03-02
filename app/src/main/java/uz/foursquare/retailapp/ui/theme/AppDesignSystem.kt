package uz.foursquare.retailapp.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle

//  colors
data class AppColorScheme(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
)

//  typography
data class AppTypography(
    val headlineH1: TextStyle,
    val headlineH2: TextStyle,
    val headlineH3: TextStyle,
    val headlineH4: TextStyle,
    val headlineH5: TextStyle,
    val bodyXL: TextStyle,
    val bodyL: TextStyle,
    val bodyM: TextStyle,
    val bodyS: TextStyle,
    val bodyXS: TextStyle,
    val actionL: TextStyle,
    val actionM: TextStyle,
    val actionS: TextStyle,
    val captionM: TextStyle
)

//  shape
data class AppShape(
    val container: Shape,
    val card: Shape,
    val button: Shape,
    val input: Shape
)

//  size

val LocalAppColorScheme = staticCompositionLocalOf {
    AppColorScheme(
        background = Color.Unspecified,
        primary = HighlightDarkest,
        onPrimary = Color.Unspecified,
        secondary = Color.Unspecified,
        onSecondary = Color.Unspecified,
        surface = Color.Unspecified
    )
}

val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        headlineH1 = TextStyle.Default,
        headlineH2 = TextStyle.Default,
        headlineH3 = TextStyle.Default,
        headlineH4 = TextStyle.Default,
        headlineH5 = TextStyle.Default,
        bodyXL = TextStyle.Default,
        bodyL = TextStyle.Default,
        bodyM = TextStyle.Default,
        bodyS = TextStyle.Default,
        bodyXS = TextStyle.Default,
        actionL = TextStyle.Default,
        actionM = TextStyle.Default,
        actionS = TextStyle.Default,
        captionM = TextStyle.Default
    )
}

val LocalAppShape = staticCompositionLocalOf {
    AppShape(
        container = RectangleShape,
        card = RectangleShape,
        button = RectangleShape,
        input = RectangleShape
    )
}

data class AppColors(
    val highlightDarkest: Color,
    val highlightDark: Color,
    val highlightMedium: Color,
    val highlightLight: Color,
    val highlightLightest: Color,
    val neutralLightDarkest: Color,
    val neutralLightDark: Color,
    val neutralLightMedium: Color,
    val neutralLightLight: Color,
    val neutralLightLightest: Color,
    val neutralDarkDarkest: Color,
    val neutralDarkDark: Color,
    val neutralDarkMedium: Color,
    val neutralDarkLight: Color,
    val neutralDarkLightest: Color,
    val supportSuccessDark: Color,
    val supportSuccessMedium: Color,
    val supportSuccessLight: Color,
    val supportWarningDark: Color,
    val supportWarningMedium: Color,
    val supportWarningLight: Color,
    val supportErrorDark: Color,
    val supportErrorMedium: Color,
    val supportErrorLight: Color
)

