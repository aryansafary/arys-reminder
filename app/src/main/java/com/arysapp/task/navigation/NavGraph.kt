package com.arysapp.task.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arysapp.task.ui.screen.AddTaskScreen
import com.arysapp.task.ui.screen.HomeScreen
import com.arysapp.task.ui.screen.SettingsScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(route = Screens.Home.route) {
            HomeScreen()
        }

        composable(route = Screens.AddTask.route) {
            AddTaskScreen(navController=navController)
        }

        composable(route = Screens.Settings.route) {
            SettingsScreen()
        }

    }

}