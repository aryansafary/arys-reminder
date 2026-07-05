package com.arysapp.reminder.navigation

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arysapp.reminder.core.alarm.AlarmIntentFactory
import com.arysapp.reminder.core.alarm.AlarmRingtoneService
import com.arysapp.reminder.core.notification.ReminderNotificationManager
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.ui.screen.AddReminderScreen
import com.arysapp.reminder.ui.screen.HomeScreen
import com.arysapp.reminder.ui.screen.SettingsScreen
import com.arysapp.reminder.ui.screen.ShowAlarmScreen
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

        composable(route = Screens.AddReminder.route + "?args={task}",
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
            val reminderModel = if (taskJson.isNullOrEmpty()) null else gson.fromJson(taskJson, ReminderModel::class.java)
            AddReminderScreen(
                navController=navController,
                reminderModel = reminderModel
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
                reminderId = taskId,
                title = title,
                description = desc,
                onStopClick = {
                    val stopIntent = Intent(context, AlarmRingtoneService::class.java).apply {
                        action = ReminderNotificationManager.ACTION_STOP_ALARM
                        putExtra(AlarmIntentFactory.EXTRA_REMINDER_ID, taskId)
                    }
                    context.startService(stopIntent)

                    navController.popBackStack()

                    val activity = context as? Activity
                    activity?.finish()
                }
            )
        }





    }

}