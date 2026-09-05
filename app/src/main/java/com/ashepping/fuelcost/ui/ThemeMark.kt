package com.ashepping.fuelcost.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ThemeMark(look: AppLook, modifier: Modifier = Modifier) {
    val ink = when (look) {
        AppLook.NEURAL_LIGHT -> Color.Black
        else -> Color.White
    }
    val fill = when (look) {
        AppLook.CURRENT -> Color(0xFF2563EB)
        AppLook.NEURAL_DARK -> Color.White
        AppLook.NEURAL_LIGHT -> Color.Black
    }
    Canvas(modifier.size(28.dp)) {
        val r = size.minDimension / 2f
        val c = Offset(size.width / 2f, size.height / 2f)
        drawCircle(color = ink, radius = r, style = Stroke(width = 2.2f * density))
        drawArc(
            color = fill,
            startAngle = -90f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(c.x - r, c.y - r),
            size = Size(r * 2f, r * 2f)
        )
        val dot = r * 0.22f
        drawCircle(color = fill, radius = dot, center = Offset(c.x, c.y - r * 0.42f))
        drawCircle(color = ink, radius = dot, center = Offset(c.x, c.y + r * 0.42f))
    }
}
