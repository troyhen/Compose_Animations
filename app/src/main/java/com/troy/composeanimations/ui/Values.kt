package com.troy.composeanimations.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.troy.composeanimations.AppBar
import com.troy.composeanimations.Nav


@Composable
fun ValueScreen(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Scaffold(topBar = { AppBar(nav, onBack) }) {
        Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            ColorButton()
        }
    }
}

/**
 * Render a colored button which will animate to a random color when clicked
 */
@Composable
private fun ColorButton() {
    var target by remember { mutableStateOf(ColorTarget.BLACK) }
    val color by animateColorAsState(targetValue = target.color)
    TextButton(onClick = { target = nextColor(target) }, colors = ButtonDefaults.textButtonColors(contentColor = target.contrast, backgroundColor = color)) {
        Text(target.title, fontSize = 20.sp)
    }
}

private fun nextColor(previous: ColorTarget): ColorTarget {
    val values = ColorTarget.values()
    var next = previous
    while (next == previous) {
        next = values[values.indices.random()]
    }
    return next
}

private enum class ColorTarget(val title: String, val color: Color, val contrast: Color = Color.Black) {
    BLACK("Black", Color.Black, Color.White),
    LIGHT_GRAY("Light Gray", Color.LightGray),
    GRAY("Gray", Color.Gray, Color.White),
    DARK_GRAY("Dark Gray", Color.DarkGray, Color.White),
    WHITE("White", Color.White),
    YELLOW("Yellow", Color.Yellow),
    ORANGE("Orange", Color(0xFFDD8800)),
    RED("Red", Color.Red, Color.White),
    MAGENTA("Magenta", Color.Magenta),
    BLUE("Blue", Color.Blue, Color.White),
    CYAN("Cyan", Color.Cyan),
    GREEN("Green", Color.Green),
}

