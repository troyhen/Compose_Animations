package com.troy.composeanimations.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.troy.composeanimations.AppBar
import com.troy.composeanimations.Nav
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun NavigationScreen(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Scaffold(topBar = { AppBar(nav, onBack) }) {
        SwipeNavigation()
    }
}

/**
 * Render a toolbar with animated tool items when selected
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwipeNavigation(modifier: Modifier = Modifier, onToolSelect: (Tool) -> Unit = {
    Log.d("SwipeNavigation", "Selected $it")
}) {
    val tools = Tool.values()
    val toolCount = tools.size

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val displayWidthPx = constraints.maxWidth
        val middlePx = displayWidthPx / 2
        val middlePxF = middlePx.toFloat()
        val toolSizeDp = maxWidth / toolCount
        val toolSizePx = displayWidthPx / toolCount
        val toolSizePxF = toolSizePx.toFloat()
        val toolMidPx = (toolSizePxF / 2).roundToInt()
        val swipeAnchors = mutableMapOf<Float, Tool>()
        tools.forEach { tool ->
            // I thought it weird that the key is the value and the value is the key
            // Start two positions off screen to the left
            swipeAnchors[toolSizePxF * (2 - tool.ordinal)] = tool
        }

        // Start with the first item
        var selectedTool by remember { mutableStateOf(Tool.HOME) }
        val swipeableState = rememberSwipeableState(selectedTool) {
            selectedTool = it
            onToolSelect(it)
            true
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .swipeable(
                    state = swipeableState,
                    anchors = swipeAnchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
                .background(Color.LightGray)
        ) {
            tools.forEach { tool ->
                val toolOffset = swipeableState.offset.value + tool.ordinal * toolSizePx
                val focusPercent = offsetToScale(toolOffset + toolMidPx, middlePxF)
                ToolItem(
                    tool = tool,
                    scale = focusPercent,
                    modifier = Modifier
                        .size(toolSizeDp)
                        .offset { IntOffset(toolOffset.roundToInt(), 0) },
                ) {
                    selectedTool = it
                }
            }
            LaunchedEffect(selectedTool) {
                swipeableState.animateTo(selectedTool)
                onToolSelect(selectedTool)
            }
        }
    }
}

private fun offsetToScale(position: Float, center: Float): Float {
    val fromCenter = (center - position).absoluteValue
    return max((center - fromCenter) / center, .5f)
}

// These are used to slow the animations a bit to make them more noticeable
private val lowStiffnessSpring = spring<Color>(stiffness = Spring.StiffnessLow)
private val linearTween = tween<Dp>(durationMillis = 100, easing = LinearEasing)

/**
 * Render a toolbar item which animates its size, color and label when selected
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ToolItem(tool: Tool, scale: Float, modifier: Modifier = Modifier, onClick: (Tool) -> Unit) {
    Box(modifier.clickable(onClick = { onClick(tool) }), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val color = if (scale > .9f) MaterialTheme.colors.primary else Color.Gray
            val size = 48.dp * scale
            val animatedColor by animateColorAsState(color, lowStiffnessSpring)
            val animatedSize by animateDpAsState(size, linearTween)
            Icon(tool.imageVector, tool.title, Modifier.size(animatedSize), tint = animatedColor)
        }
    }
}