package com.example.kalagatotask.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kalagatotask.component.CircularRevealLayout
import com.example.kalagatotask.component.CommonScreenHeading
import com.example.kalagatotask.component.DropdownMenuList
import com.example.kalagatotask.component.ListItemScreen
import com.example.kalagatotask.component.getPriorityValue
import com.example.kalagatotask.navigation.NavScreen
import com.example.kalagatotask.viewmodel.TaskViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: TaskViewModel = viewModel()
    val taskList by viewModel.tasks.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedSort by remember { mutableStateOf("Due Date") }
    var selectedFilter by remember { mutableStateOf("All") }

    val totalTasks = taskList.size
    val completedTasks = taskList.count { it.isCompleted }
    val progress = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f

    var showReveal by remember { mutableStateOf(false) }
    var navigateAfterAnimation by remember { mutableStateOf(false) }
    var fabScale by remember { mutableFloatStateOf(1f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Column {
            CommonScreenHeading(
                "Task Manager",
                navController,
                isIconVisible = false,
                isSettingIconVisible = true,
                isSettingIconClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "completedProgress",
                        progress
                    )
                    navController.navigate(NavScreen.Settings.route)
                })
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                DropdownMenuList(
                    { selectedSort = it },
                    listOf("Due Date", "Priority", "Alphabetical"),
                    selectedSort
                )
                Spacer(modifier = Modifier.width(10.dp))
                DropdownMenuList(
                    { selectedFilter = it },
                    listOf("All", "Completed", "Pending"),
                    selectedFilter
                )
            }

            val sortedTasks = when (selectedSort) {
                "Priority" -> taskList.sortedBy { getPriorityValue(it.priority) }
                "Alphabetical" -> taskList.sortedBy { it.title }
                else -> taskList.sortedBy { it.dueDate }
            }

            val filteredTasks = when (selectedFilter) {
                "Completed" -> sortedTasks.filter { it.isCompleted }
                "Pending" -> sortedTasks.filter { !it.isCompleted }
                else -> sortedTasks
            }

            ListItemScreen(filteredTasks,isLoading, viewModel, navController = navController)
        }
        if (taskList.isNotEmpty()) {
            val scale by animateFloatAsState(
                targetValue = if (fabScale == 1f) 0.7f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                ), label = ""
            )
            FloatingActionButton(
                onClick = {
                    fabScale = 0.9f
                    showReveal = true
                },
                modifier = Modifier
                    .padding(bottom = 50.dp, end = 10.dp)
                    .align(Alignment.BottomEnd)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
        if (showReveal) {
            CircularRevealLayout(
                modifier = Modifier.fillMaxSize(),
                onAnimationEnd = {
                    navigateAfterAnimation = true
                }
            )
        }
        if (navigateAfterAnimation) {
            LaunchedEffect(true) {
                delay(10)
                navController.navigate(NavScreen.AddTask.route)
            }
        }
    }
}
