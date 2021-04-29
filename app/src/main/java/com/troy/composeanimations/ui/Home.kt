package com.troy.composeanimations.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.troy.composeanimations.AppBar
import com.troy.composeanimations.Nav
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun HomeScreen(nav: Nav, onNavigate: (Nav) -> Unit) {
    Scaffold(topBar = { AppBar(nav) }) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ClockAnimation(
                Modifier
                    .size(100.dp)
            )
            Nav.values().filterNot { it == Nav.HOME}.forEach { destination ->
                TextButton(
                    onClick = { onNavigate(destination) },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.textButtonColors(backgroundColor = MaterialTheme.colors.surface)
                ) {
                    Text(destination.title)
                }
            }
        }
    }
}

/**
 * Render an animated clock, but with one hand going clockwise and another going counterclockwise.
 * If the clock is clicked the hands will reset and spin in the opposite directions.
 * This shows an example of using infinite transition animations.
 */
@Composable
private fun ClockAnimation(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    var direction by remember { mutableStateOf(true) }
    val pi2 = 2 * PI.toFloat()
    val startAngle = if (direction) 0f else pi2
    val endAngle = if (direction) pi2 else 0f
    val forwardAngle by transition.animateFloat(
        initialValue = startAngle,
        targetValue = endAngle,
        animationSpec = infiniteRepeatable(animation = tween(10_000, easing = LinearEasing))
    )
    val backwardAngle by transition.animateFloat(
        initialValue = endAngle,
        targetValue = startAngle,
        animationSpec = infiniteRepeatable(animation = tween(60_000, easing = LinearEasing))
    )
    val handColor = MaterialTheme.colors.primaryVariant
    val frameColor = MaterialTheme.colors.primaryVariant
    val backColor = MaterialTheme.colors.surface
    BoxWithConstraints(modifier.clickable { direction = !direction }) {
        val size = min(maxHeight, maxWidth)
        with(LocalDensity.current) {
            val radius = (size / 2).toPx()
            val radius1 = radius * .92f
            val radius2 = radius * .75f
            val center = Offset(radius, radius)
            val stroke1 = 2.dp.toPx()
            val stroke2 = 3.dp.toPx()
            val frameStyle = Stroke(stroke1)

            Canvas(Modifier.fillMaxSize()) {
                val x1 = radius + radius1 * sin(forwardAngle)
                val y1 = radius - radius1 * cos(forwardAngle)
                val x2 = radius + radius2 * sin(backwardAngle)
                val y2 = radius - radius2 * cos(backwardAngle)
                drawCircle(backColor, radius, center)
                drawLine(handColor, center, Offset(x1, y1), stroke1, StrokeCap.Round)
                drawLine(handColor, center, Offset(x2, y2), stroke2, StrokeCap.Round)
                drawCircle(frameColor, radius, center, style = frameStyle)
            }
        }
    }
}
