package com.troy.composeanimations.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.troy.composeanimations.AppBar
import com.troy.composeanimations.Nav
import kotlin.math.roundToInt

@Composable
fun NavigationScreen(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Scaffold(topBar = { AppBar(nav, onBack) }) {
        Navigation()
    }
}

/**
 * Render a toolbar with animated tool items when selected
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Navigation(modifier: Modifier = Modifier) {
    val toolCount = Tool.values().size

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val displayWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val squareSizeDp = maxWidth / toolCount
        val squareSizePx = with(LocalDensity.current) { squareSizeDp.toPx() }
        val swipeAnchors = mutableMapOf<Float, Int>()
        // Start two positions off screen to the left
        val startAnchorPosition = -squareSizePx * 2
        for (i in 0 until toolCount) {
            // I thought it weird that the key is the value and the value is the key
            swipeAnchors[startAnchorPosition + (squareSizePx * i)] = i
        }

        // Start with the first item in the center
        val swipeableState: SwipeableState<Int> = rememberSwipeableState(4)

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
            Tool.values().forEachIndexed { index, tool ->
                val toolSwipeOffset = (index * squareSizePx).toInt()
                val toolDisplayOffset = swipeableState.offset.value.roundToInt() + toolSwipeOffset
                val fromCenter = distanceFromCenter(toolDisplayOffset + (squareSizePx.toInt() / 2), displayWidthPx.toInt())
                ToolItem(
                    tool = tool,
                    offset = kotlin.math.max((432 - fromCenter) / 432F, .5F),
                    modifier = Modifier
                        .size(squareSizeDp)
                        .offset { IntOffset(toolDisplayOffset, 0) }
                        .fillMaxHeight(),
                )
            }
        }

    }
}

private fun distanceFromCenter(position: Int, width: Int): Int {
    val center = width / 2
    return if (position <= center) {
        center - position
    } else {
        position - center
    }
}

// These are used to slow the animations a bit to make them more noticeable
private val lowStiffnessSpring = spring<Color>(stiffness = Spring.StiffnessLow)
private val linearTween = tween<Dp>(durationMillis = 100, easing = LinearEasing)

/**
 * Render a toolbar item which animates its size, color and label when selected
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ToolItem(tool: Tool, offset: Float, modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val color = if (offset > .9F) MaterialTheme.colors.primary else Color.Gray
            val size = 48.dp * offset
            val animatedColor by animateColorAsState(color, lowStiffnessSpring)
            val animatedSize by animateDpAsState(size, linearTween)
            Icon(tool.imageVector, tool.title, Modifier.size(animatedSize), tint = animatedColor)
        }
    }
}