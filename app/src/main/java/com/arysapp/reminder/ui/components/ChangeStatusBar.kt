package com.arysapp.reminder.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.arysapp.reminder.navigation.Screens
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ChangeStatusBarColor(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val systemUiController = rememberSystemUiController()
    when (backStackEntry?.destination?.route) {
        Screens.Home.route -> {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
            )

        }
    }
}