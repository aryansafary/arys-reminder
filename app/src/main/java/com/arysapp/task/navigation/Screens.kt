package com.arysapp.task.navigation

sealed class Screens(val route: String) {
    object Home: Screens(route = "HomeScreen")

}