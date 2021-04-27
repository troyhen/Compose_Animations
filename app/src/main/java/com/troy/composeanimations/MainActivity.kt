package com.troy.composeanimations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.troy.composeanimations.ui.theme.AppTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var nav by remember { mutableStateOf(Nav.HOME) }
            Screen(nav = nav) { nav = it }
        }
    }
}

enum class Nav(val title: String) {
    HOME("Compose Animations"),

}


@Composable
private fun Screen(nav: Nav, onNavigate: (Nav) -> Unit) {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            when (nav) {
                Nav.HOME -> HomeScreen(nav, onNavigate)
            }
        }
    }
}

@Composable
private fun HomeScreen(nav: Nav, onNavigate: (Nav) -> Unit) {
    Scaffold(topBar = { AppBar(nav) }) {
        Column(Modifier.fillMaxWidth().padding(8.dp)) {
            ClockAnimation(
                Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally))
        }
    }
}

/**
 * Render an animated clock, but with one hand going clockwise and another going counterclockwise. If the clock is clicked the hands will spin in the opposite directions.
 */
@Composable
private fun ClockAnimation(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()           // animation that doesn't end
    var direction by remember { mutableStateOf(true) }// control direction
    val pi2 = 2 * PI.toFloat()
    val startAngle = if (direction) 0f else pi2
    val endAngle = if (direction) pi2 else 0f
    val forwardAngle by transition.animateFloat(initialValue = startAngle, targetValue = endAngle, animationSpec = infiniteRepeatable(animation = tween(10_000, easing = LinearEasing)))
    val backwardAngle by transition.animateFloat(initialValue = endAngle, targetValue = startAngle, animationSpec = infiniteRepeatable(animation = tween(60_000, easing = LinearEasing)))
    val handColor = MaterialTheme.colors.primary
    val frameColor = MaterialTheme.colors.primaryVariant
    val backColor = MaterialTheme.colors.secondary
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
                val x1 = radius - radius1 * sin(forwardAngle)
                val y1 = radius - radius1 * cos(forwardAngle)
                val x2 = radius - radius2 * sin(backwardAngle)
                val y2 = radius - radius2 * cos(backwardAngle)
                drawCircle(backColor, radius, center)
                drawLine(handColor, center, Offset(x1, y1), stroke1, StrokeCap.Round)
                drawLine(handColor, center, Offset(x2, y2), stroke2, StrokeCap.Round)
                drawCircle(frameColor, radius, center, style = frameStyle)
            }
        }
    }
}

@Composable
private fun AppBar(nav: Nav) {
    TopAppBar(title = { Text(text = nav.title) })
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        Greeting("Android")
    }
}