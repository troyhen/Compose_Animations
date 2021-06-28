package com.troy.composeanimations.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.troy.composeanimations.AppBar
import com.troy.composeanimations.Nav


@Composable
fun ValueScreen(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Scaffold(topBar = { AppBar(nav, onBack) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ColorButton()
            ToolBar(
                Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(MaterialTheme.colors.surface)
            )
        }
    }
}

// These are used to slow the animations a bit to make them more noticeable
private val colorSpring = spring<Color>(stiffness = Spring.StiffnessLow)
private val dpTween = tween<Dp>(durationMillis = 750)

/**
 * Render a colored button which will animate to a random color when clicked
 */
@Composable
private fun ColorButton() {
    var target by remember { mutableStateOf(ColorTarget.BLACK) }
    val color by animateColorAsState(target.color, colorSpring)
    TextButton(onClick = { target = nextColor(target) }, colors = ButtonDefaults.textButtonColors(contentColor = target.contrast, backgroundColor = color)) {
        Text(target.title, fontSize = 20.sp)
    }
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

private fun nextColor(previous: ColorTarget): ColorTarget {
    val values = ColorTarget.values()
    var next = previous
    while (next == previous) {
        next = values[values.indices.random()]
    }
    return next
}

enum class Tool(val title: String, val imageVector: ImageVector) {
    HOME("Home", Icons.Default.Home),
    SEARCH("Search", Icons.Default.Search),
    CART("Cart", Icons.Default.ShoppingCart),
    ORDERS("Orders", Icons.Default.AccountBox),
    SETTINGS("Settings", Icons.Default.Settings),
}

/**
 * Render a toolbar with animated tool items when selected
 */
@Composable
private fun ToolBar(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf(Tool.HOME) }
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Tool.values().forEach { tool ->
            ToolItem(
                tool = tool, selected = selected == tool,
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) { selected = tool }
        }
    }
}

/**
 * Render a toolbar item which animates its size, color and label when selected
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ToolItem(tool: Tool, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(modifier.clickable { onClick() }, contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val color = if (selected) MaterialTheme.colors.primary else Color.Gray
            val size = if (selected) 30.dp else 24.dp
            val animatedColor by animateColorAsState(color, colorSpring)
            val animatedSize by animateDpAsState(size, dpTween)
            Icon(tool.imageVector, tool.title, Modifier.size(animatedSize), tint = animatedColor)
            AnimatedVisibility(visible = selected) {
                Text(tool.title, color = animatedColor, style = MaterialTheme.typography.caption)
            }
        }
    }
}
