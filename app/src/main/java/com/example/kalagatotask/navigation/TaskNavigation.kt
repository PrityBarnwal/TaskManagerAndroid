package com.example.kalagatotask.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kalagatotask.screen.AddTaskScreen
import com.example.kalagatotask.screen.HomeScreen
import com.example.kalagatotask.screen.SettingScreen
import com.example.kalagatotask.screen.TaskDetailScreen


@Composable
fun NavGraph(isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavScreen.Home.route) {
        composable(NavScreen.Home.route) {
            HomeScreen(navController)
        }
        composable(
            route = NavScreen.AddTask.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 800 },
                    animationSpec = tween(500)
                )
            },
            exitTransition = { fadeOut(animationSpec = tween(700)) }
        ) {
            AddTaskScreen(navController = navController)
        }

        composable(NavScreen.Settings.route) {
            val completedProgress = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Float>("completedProgress") ?: 0f

            SettingScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                completedProgress = completedProgress
            )
        }

        composable(
            route = "${NavScreen.TaskDetails.route}/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
            TaskDetailScreen(navController, taskId)
        }
    }
}