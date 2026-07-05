package com.arysapp.reminder.navigation

sealed class Screens(val route: String) {
    object Home: Screens(route = "HomeScreen")
    object Settings : Screens(route = "SettingsScreen")
    object AddReminder : Screens(route = "AddReminderScreen")
    object ShowAlarmScreen : Screens(route = "ShowAlarmScreen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("?args=$arg")
            }
        }
    }
}