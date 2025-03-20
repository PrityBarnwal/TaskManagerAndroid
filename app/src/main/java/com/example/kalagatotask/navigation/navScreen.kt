package com.example.kalagatotask.navigation

sealed class NavScreen(val route: String) {
    object Home : NavScreen("home")
    object AddTask : NavScreen("add_task")
    object TaskDetails : NavScreen("task_detail")
    object Settings : NavScreen("settings")
}