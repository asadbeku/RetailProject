package uz.foursquare.retailapp.ui.theme

import android.os.Build
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkColorScheme = AppColorScheme(
    primary = HighlightDarkest,
    secondary = HighlightDark,
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.White
)

private val LightColorScheme = AppColorScheme(
    primary = HighlightDarkest,
    secondary = HighlightDark,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White
)

private val appColors = AppColors(
    highlightDarkest = HighlightDarkest,
    highlightDark = HighlightDark,
    highlightMedium = HighlightMedium,
    highlightLight = HighlightLight,
    highlightLightest = HighlightLightest,
    neutralLightDarkest = NeutralLightDarkest,
    neutralLightDark = NeutralLightDark,
    neutralLightMedium = NeutralLightMedium,
    neutralLightLight = NeutralLightLight,
    neutralLightLightest = NeutralLightLightest,
    neutralDarkDarkest = NeutralDarkDarkest,
    neutralDarkDark = NeutralDarkDark,
    neutralDarkMedium = NeutralDarkMedium,
    neutralDarkLight = NeutralDarkLight,
    neutralDarkLightest = NeutralDarkLightest,
    supportSuccessDark = SupportSuccessDark,
    supportSuccessMedium = SupportSuccessMedium,
    supportSuccessLight = SupportSuccessLight,
    supportWarningDark = SupportWarningDark,
    supportWarningMedium = SupportWarningMedium,
    supportWarningLight = SupportWarningLight,
    supportErrorDark = SupportErrorDark,
    supportErrorMedium = SupportErrorMedium,
    supportErrorLight = SupportErrorLight
)

private val typography = AppTypography(
    headlineH1 = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp
    ),
    headlineH2 = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp
    ),
    headlineH3 = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp
    ),
    headlineH4 = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    headlineH5 = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),
    bodyXL = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    bodyL = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyM = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodyS = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    bodyXS = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp
    ),
    actionL = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    actionM = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    ),
    actionS = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp
    ),
    captionM = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp
    )
)

private val shape = AppShape(
    container = RoundedCornerShape(16.dp),
    card = RoundedCornerShape(16.dp),
    button = RoundedCornerShape(8.dp),
    input = RoundedCornerShape(8.dp)
)

@Composable
fun RetailAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val rippleIndication =
        ripple(color = colorScheme.onPrimary, radius = 24.dp, bounded = true)

    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides typography,
        LocalAppShape provides shape,
        LocalIndication provides rippleIndication,
        content = content
    )
}

object AppTheme {
    val color: AppColorScheme
        @Composable get() = LocalAppColorScheme.current

    val typography: AppTypography
        @Composable get() = LocalAppTypography.current

    val shape: AppShape
        @Composable get() = LocalAppShape.current

    val appColor: AppColors
        @Composable get() = appColors
}