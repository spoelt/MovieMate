package com.spoelt.moviemate.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.spoelt.moviemate.R

private val LightColors = lightColors(
    primary = Color.Black,
    background = Color.White,
    surface = Color.White,
)

private val fontFamily = FontFamily(
    Font(R.font.sf_pro_bold, FontWeight.Bold),
    Font(R.font.sf_pro_regular, FontWeight.Normal),
)

private val Typography = Typography(
    body1 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.0.sp
    ),
    body2 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    caption = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )
)

@Composable
fun MovieMateTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColors,
        typography = Typography,
        content = content
    )
}

fun Color.withAlpha(alpha: Float) = this.copy(alpha = alpha)