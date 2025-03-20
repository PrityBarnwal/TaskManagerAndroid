package com.example.kalagatotask.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


data class CustomColors(
    val blackWhite: Color,
    val grayLightGray: Color,
    val buttonColor: Color,
)

// Light Theme Colors
val LightCustomColors = CustomColors(
    blackWhite = Color(0xFF000000), // Teal
    grayLightGray = Color.Gray,
    buttonColor = Color(0xFF5497CC)
)

// Dark Theme Colors
val DarkCustomColors = CustomColors(
    blackWhite = Color(0xFFFFFFFF), // Gold
    grayLightGray = Color.LightGray,
    buttonColor = Color(0xFF6DC472),
)