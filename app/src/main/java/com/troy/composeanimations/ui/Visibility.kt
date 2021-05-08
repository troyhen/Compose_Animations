package com.troy.composeanimations.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.troy.composeanimations.AppBar
import com.troy.composeanimations.Nav


@Composable
fun VisibilityScreen(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Scaffold(topBar = { AppBar(nav, onBack) }) {
        BoxWithConstraints {
            val width = constraints.maxWidth
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            ) {
                FadeRow(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(40.dp))
                SlideRow(width,
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(40.dp))
                ExpandRow(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(40.dp))
            }

        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun FadeRow(modifier: Modifier = Modifier) {
    val spec = tween<Float>(durationMillis = 1000) // slow animation down
    var isVisible by remember { mutableStateOf(false) }
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        val text = if (isVisible) "Fade Out" else "Fade In"
        Text(text, Modifier.clickable { isVisible = !isVisible })
        Spacer(modifier = Modifier.width(20.dp))
        AnimatedVisibility(visible = isVisible, enter = fadeIn(animationSpec = spec), exit = fadeOut(animationSpec = spec)) {
            Box(
                Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colors.secondary))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SlideRow(width: Int, modifier: Modifier = Modifier) {
    val spec = tween<IntOffset>(durationMillis = 1000) // slow animation down
    var isVisible by remember { mutableStateOf(false) }
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        val text = if (isVisible) "Slide Out" else "Slide In"
        Text(text, Modifier.clickable { isVisible = !isVisible })
        Spacer(modifier = Modifier.width(20.dp))
        AnimatedVisibility(visible = isVisible, enter = slideInHorizontally({ width }, animationSpec = spec), exit = slideOutHorizontally({ width }, animationSpec = spec)) {
            Box(
                Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colors.secondary))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ExpandRow(modifier: Modifier = Modifier) {
    val spec = tween<IntSize>(durationMillis = 1000) // slow animation down
    var isVisible by remember { mutableStateOf(false) }
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        val text = if (isVisible) "Shrink Out" else "Expand In"
        Text(text, Modifier.clickable { isVisible = !isVisible })
        Spacer(modifier = Modifier.width(20.dp))
        AnimatedVisibility(visible = isVisible, enter = expandHorizontally(animationSpec = spec), exit = shrinkHorizontally(animationSpec = spec)) {
            Box(
                Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colors.secondary))
        }
    }
}
