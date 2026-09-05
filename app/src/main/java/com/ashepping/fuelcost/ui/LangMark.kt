package com.ashepping.fuelcost.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun LangMark(look: AppLook, modifier: Modifier = Modifier) {
    val ink = when (look) {
        AppLook.NEURAL_LIGHT -> Color.Black
        else -> Color.White
    }
    Canvas(modifier.size(28.dp)) {
        val stroke = 2.1f * density
        val r = size.minDimension / 2f - stroke
        val c = Offset(size.width / 2f, size.height / 2f)
        drawCircle(color = ink, radius = r, center = c, style = Stroke(width = stroke))
        drawOval(
            color = ink,
            topLeft = Offset(c.x - r * 0.42f, c.y - r),
            size = Size(r * 0.84f, r * 2f),
            style = Stroke(width = stroke)
        )
        drawLine(
            color = ink,
            start = Offset(c.x - r, c.y),
            end = Offset(c.x + r, c.y),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = ink,
            start = Offset(c.x - r * 0.86f, c.y - r * 0.48f),
            end = Offset(c.x + r * 0.86f, c.y - r * 0.48f),
            strokeWidth = stroke * 0.9f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = ink,
            start = Offset(c.x - r * 0.86f, c.y + r * 0.48f),
            end = Offset(c.x + r * 0.86f, c.y + r * 0.48f),
            strokeWidth = stroke * 0.9f,
            cap = StrokeCap.Round
        )
    }
}
