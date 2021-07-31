package ua.hospes.rtm.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Yellow400 = Color(0xFFF6E547)
private val Yellow600 = Color(0xFFF5CF1B)
private val Yellow700 = Color(0xFFF3B711)
private val Yellow800 = Color(0xFFF29F05)

private val Blue200 = Color(0xFF9DA3FA)
private val Blue400 = Color(0xFF4860F7)
private val Blue500 = Color(0xFF0540F2)
private val Blue800 = Color(0xFF001CCF)

private val Red300 = Color(0xFFEA6D7E)
private val Red800 = Color(0xFFD00036)

private val Orange = Color(0xFFFF6859)

private val RTMDarkPalette = darkColors(
    primary = Blue200,
    primaryVariant = Blue400,
    onPrimary = Color.Black,
    secondary = Orange,
    onSecondary = Color.Black,
    surface = Color(0x338b8b8b),
    onSurface = Color.White,
    onBackground = Color.White,
    error = Red300,
    onError = Color.Black
)

private val RTMLightPalette = lightColors(
    primary = Blue500,
    primaryVariant = Blue800,
    onPrimary = Color.White,
    secondary = Orange,
    secondaryVariant = Yellow800,
    onSecondary = Color.Black,
    surface = Color(0x338b8b8b),
    onSurface = Color.White,
    onBackground = Color.Black,
    error = Red800,
    onError = Color.White
)

@Composable
fun RTMTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    colors: Colors? = null,
    content: @Composable () -> Unit
) {
    val myColors = colors ?: if (isDarkTheme) RTMDarkPalette else RTMLightPalette

    MaterialTheme(
        colors = myColors,
        content = content,
        typography = RTMTypography,
        shapes = RTMShapes
    )
}