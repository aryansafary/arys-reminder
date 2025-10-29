package com.arysapp.task.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.ui.screen.AddTaskScreen
import com.arysapp.task.ui.screen.HomeScreen
import com.arysapp.task.ui.screen.SettingsScreen
import com.google.gson.Gson

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(route = Screens.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(route = Screens.AddTask.route + "?args={task}",
            arguments = listOf(
                navArgument("task") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
            ) {
            val gson = Gson()
            val taskJson = it.arguments?.getString("task")
            val taskModel = if (taskJson.isNullOrEmpty()) null else gson.fromJson(taskJson, TaskModel::class.java)
            AddTaskScreen(
                navController=navController,
                taskModel = taskModel
                )
        }

        composable(route = Screens.Settings.route) {
            SettingsScreen()
        }

    }

}