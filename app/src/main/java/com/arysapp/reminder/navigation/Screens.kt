package com.arysapp.reminder.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween

sealed class Screens(
    val route: String,
    val enterTransition: (AnimatedContentTransitionScope<*>.() -> EnterTransition?)? = {
        fadeIn(tween(300)) + slideInHorizontally { 100 }
    },
    val exitTransition: (AnimatedContentTransitionScope<*>.() -> ExitTransition?)? = {
        fadeOut(tween(300)) + slideOutHorizontally { -100 }
    },
    val popEnterTransition: (AnimatedContentTransitionScope<*>.() -> EnterTransition?)? = {
        fadeIn(tween(300)) + slideInHorizontally { -100 }
    },
    val popExitTransition: (AnimatedContentTransitionScope<*>.() -> ExitTransition?)? = {
        fadeOut(tween(300)) + slideOutHorizontally { 100 }
    }
) {
    object Home : Screens(
        route = "HomeScreen",
        enterTransition = { fadeIn(tween(400)) },
        exitTransition = { fadeOut(tween(400)) }
    )
    object Settings : Screens(route = "SettingsScreen")
    object AddReminder : Screens(route = "AddReminderScreen") {
        fun withArgs(taskJson: String): String = "$route?task=$taskJson"
    }
    object ShowAlarmScreen : Screens(route = "ShowAlarmScreen") {
//        fun withArgs(reminderId: Long, title: String, desc: String): String =
//            "$route?reminderId=$reminderId&title=$title&desc=$desc"
    }
    object Calender : Screens(route = "CalenderScreen")
}