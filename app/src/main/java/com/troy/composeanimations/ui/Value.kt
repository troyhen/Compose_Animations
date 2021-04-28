package com.troy.composeanimations.ui

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.troy.composeanimations.AppBar
import com.troy.composeanimations.Nav


@Composable
fun ValueScreen(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Scaffold(topBar = { AppBar(nav, onBack) }) {

    }
}
