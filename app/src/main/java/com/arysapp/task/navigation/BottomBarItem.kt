package com.arysapp.task.navigation

import androidx.compose.ui.graphics.painter.Painter

data class BottomBarItem(
    val name: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val route: String

)
