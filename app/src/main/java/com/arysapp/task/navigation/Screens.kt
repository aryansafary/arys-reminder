package com.arysapp.task.navigation

sealed class Screens(val route: String) {
    object Home: Screens(route = "HomeScreen")
    object Calendar : Screens(route = "CalendarScreen")
    object Settings : Screens(route = "SettingsScreen")
}