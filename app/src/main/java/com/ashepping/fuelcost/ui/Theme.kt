package com.ashepping.fuelcost.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppLook { CURRENT, NEURAL_DARK, NEURAL_LIGHT }

private val Navy = Color(0xFF0F172A)
private val Card = Color(0xFF1E293B)
private val Accent = Color(0xFF2563EB)
private val Good = Color(0xFF059669)
private val ChipOn = Color(0xFF5B5470)

@Composable
fun FuelTheme(look: AppLook = AppLook.CURRENT, content: @Composable () -> Unit) {
    val colors = when (look) {
        AppLook.CURRENT -> darkColorScheme(
            background = Navy,
            surface = Card,
            primary = Accent,
            secondary = Good,
            secondaryContainer = ChipOn,
            onSecondaryContainer = Color.White,
            onBackground = Color.White,
            onSurface = Color.White,
            onPrimary = Color.White
        )
        AppLook.NEURAL_DARK -> darkColorScheme(
            background = Color.Black,
            surface = Color(0xFF111111),
            primary = Color.White,
            secondary = Color.White,
            secondaryContainer = Color(0xFF2A2A2A),
            onSecondaryContainer = Color.White,
            onBackground = Color.White,
            onSurface = Color.White,
            onPrimary = Color.Black
        )
        AppLook.NEURAL_LIGHT -> lightColorScheme(
            background = Color.White,
            surface = Color(0xFFF4F4F4),
            primary = Color.Black,
            secondary = Color.Black,
            secondaryContainer = Color(0xFFE6E6E6),
            onSecondaryContainer = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black,
            onPrimary = Color.White
        )
    }
    MaterialTheme(colorScheme = colors, content = content)
}
