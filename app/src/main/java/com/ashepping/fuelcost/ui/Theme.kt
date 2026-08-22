package com.ashepping.fuelcost.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Navy = Color(0xFF0F172A)
private val Card = Color(0xFF1E293B)
private val Accent = Color(0xFF2563EB)
private val Good = Color(0xFF059669)

@Composable
fun FuelTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Navy,
            surface = Card,
            primary = Accent,
            secondary = Good,
            onBackground = Color.White,
            onSurface = Color.White,
            onPrimary = Color.White
        ),
        content = content
    )
}
