package com.troy.composeanimations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.troy.composeanimations.ui.AnimatableScreen
import com.troy.composeanimations.ui.DraggingScreen
import com.troy.composeanimations.ui.HomeScreen
import com.troy.composeanimations.ui.NavigationScreen
import com.troy.composeanimations.ui.SwipingScreen
import com.troy.composeanimations.ui.ValueScreen
import com.troy.composeanimations.ui.VisibilityScreen
import com.troy.composeanimations.ui.theme.AppTheme


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
    VISIBILITY("Animate Visibility"),
    VALUE("Animate Values"),
    ANIMATABLE("Animatable"),
    SWIPABLE_NAV("Animate Navigation"),
    DRAGGING("Dragging"),
    SWIPABLE("Swipable"),
}

@Composable
private fun Screen(nav: Nav, onNavigate: (Nav) -> Unit) {
    val onBack: () -> Unit = { onNavigate(Nav.HOME) }
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            when (nav) {
                Nav.HOME -> HomeScreen(nav, onNavigate)
                Nav.VISIBILITY -> VisibilityScreen(nav, onBack)
                Nav.VALUE -> ValueScreen(nav, onBack)
                Nav.ANIMATABLE -> AnimatableScreen(nav, onBack)
                Nav.SWIPABLE_NAV -> NavigationScreen(nav, onBack)
                Nav.DRAGGING -> DraggingScreen(nav, onBack)
                Nav.SWIPABLE -> SwipingScreen(nav, onBack)
            }
        }
    }
}

@Composable
fun AppBar(nav: Nav, onBack: () -> Unit = {}) {
    val navIcon: (@Composable () -> Unit)? = if (nav == Nav.HOME) null else {
        {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.Default.ArrowBack, "Back")
            }
        }
    }
    TopAppBar(title = { Text(text = nav.title) }, navigationIcon = navIcon)
}
