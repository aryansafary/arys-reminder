package com.arysapp.task.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arysapp.task.core.alarm.AlarmIntentFactory
import com.arysapp.task.core.alarm.AlarmRingtoneService
import com.arysapp.task.core.notification.TaskNotificationManager
import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.ui.screen.AddTaskScreen
import com.arysapp.task.ui.screen.HomeScreen
import com.arysapp.task.ui.screen.SettingsScreen
import com.arysapp.task.ui.screen.ShowAlarmScreen
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

        composable(
            route = Screens.ShowAlarmScreen.route + "?taskId={taskId}&title={title}&desc={desc}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument("title") {
                    type = NavType.StringType
                    defaultValue = "آلارم تسک"
                },
                navArgument("desc") {
                    type = NavType.StringType
                    defaultValue = "زمان انجام فعالیت فرا رسیده است."
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val desc = backStackEntry.arguments?.getString("desc") ?: ""
            val context = LocalContext.current

            ShowAlarmScreen(
                taskId = taskId,
                title = title,
                description = desc,
                onStopClick = {
                    val stopIntent = Intent(context, AlarmRingtoneService::class.java).apply {
                        action = TaskNotificationManager.ACTION_STOP_ALARM
                        putExtra(AlarmIntentFactory.EXTRA_TASK_ID, taskId)
                    }
                    context.startService(stopIntent)

                    navController.popBackStack()
                }
            )
        }





    }

}