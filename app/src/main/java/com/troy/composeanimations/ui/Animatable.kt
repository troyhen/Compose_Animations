package com.troy.composeanimations.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.troy.composeanimations.AppBar
import com.troy.composeanimations.Nav


@Composable
fun AnimatableScreen(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Scaffold(topBar = { AppBar(nav, onBack) }) {
        BouncingBox(100.dp, Color.Cyan)
    }
}

/**
 * Render an animated box which rotates and bounces off the device edges.
 */
@Composable
fun BouncingBox(size: Dp, color: Color) {
    val horizontal = remember { Animatable(initialValue = 0f) }
    val vertical = remember { Animatable(initialValue = 0f) }
    val rotation = remember { Animatable(initialValue = 0f) }
    BoxWithConstraints {
        LaunchedEffect(horizontal) {
            horizontal.animateTo(
                targetValue = maxWidth.value - size.value,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2_000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
        LaunchedEffect(vertical) {
            vertical.animateTo(
                targetValue = maxHeight.value - size.value,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 5_000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
        LaunchedEffect(rotation) {
            rotation.animateTo(
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 10_000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
        Box(
            Modifier
                .offset(horizontal.value.dp, vertical.value.dp)
                .rotate(rotation.value)
                .size(size)
                .background(color), contentAlignment = Alignment.Center
        ) {
            Text("Bouncing Box", fontSize = 20.sp, textAlign = TextAlign.Center)
        }
    }
}
