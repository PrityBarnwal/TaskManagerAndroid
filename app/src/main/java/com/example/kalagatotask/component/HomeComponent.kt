package com.example.kalagatotask.component

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kalagatotask.navigation.NavScreen
import com.example.kalagatotask.room.TaskEntity
import com.example.kalagatotask.ui.theme.PurpleGrey80
import com.example.kalagatotask.ui.theme.customColors
import com.example.kalagatotask.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.Gray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
            ),

        ), label = ""
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim.value, translateAnim.value),
        end = Offset(translateAnim.value + 300f, translateAnim.value + 300f)
    )
}

@Composable
fun ShimmerListItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
    )
}


@Composable
fun ListItemScreen(task: List<TaskEntity>, isLoading: Boolean,viewModel: TaskViewModel, navController: NavController) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val taskItemData = remember { mutableStateListOf<TaskEntity>() }

    LaunchedEffect(task) {
        taskItemData.clear()
        taskItemData.addAll(task)
    }

    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to ->
            val item = taskItemData.removeAt(from.index)
            taskItemData.add(to.index, item)
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { padding ->
        if (isLoading) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                items(8) {
                    ShimmerListItem()
                }
            }
        } else if (taskItemData.isEmpty()) {
            EmptyState(navController)
        } else {
            LazyColumn(
                state = reorderState.listState,
                modifier = Modifier.padding(horizontal = 8.dp)
                    .reorderable(reorderState)
            ) {
                items(taskItemData, key = { it.id }) { item ->
                    ReorderableItem(reorderState, key = item.id) { isDragging ->
                        ListItem(
                            itemName = item,
                            viewModel = viewModel,
                            onRemove = {
                                scope.launch {
                                    val result = snackBarHostState.showSnackbar(
                                        message = "Task deleted",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.addTask(item)
                                    }
                                }
                            },
                            onComplete = {
                                scope.launch {
                                    val result = snackBarHostState.showSnackbar(
                                        message = "Task marked as completed",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.addTask(item)
                                    }
                                }
                            },
                            modifier = Modifier
                                .detectReorderAfterLongPress(reorderState)
                                .alpha(if (isDragging) 0.6f else 1f),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(
    itemName: TaskEntity,
    viewModel: TaskViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onRemove: () -> Unit,
    onComplete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onRemove()
                    viewModel.deleteTask(itemName)
                    true
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    onComplete()
                    viewModel.deleteTask(itemName)
                    true
                }

                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier.padding(vertical = 4.dp),
        backgroundContent = { DismissBackground(dismissState) },
        content = {
            TaskItemCard(itemName, navController = navController)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val icon = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Delete
        SwipeToDismissBoxValue.EndToStart -> Icons.Default.Check
        else -> null
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
            Arrangement.Start
        } else {
            Arrangement.End
        }
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = "Swipe action",
                tint = Color.Red,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}


@Composable
fun EmptyState(navController: NavController) {
    Column(
        modifier = Modifier
            .clickable {
                navController.navigate(NavScreen.AddTask.route)
            }
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = "No Tasks",
            modifier = Modifier.size(100.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No tasks yet!",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Get started by adding your first task and stay organized.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DropdownMenuList(
    onSelect: (String) -> Unit,
    options: List<String>,
    selectedOption: String
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = selectedOption,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Arrow"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = option,
                            color = if (option == selectedOption) MaterialTheme.customColors.buttonColor else MaterialTheme.customColors.blackWhite
                        )
                    }
                )
            }
        }
    }
}


fun getPriorityValue(priority: String): Int {
    return when (priority) {
        "High" -> 1
        "Medium" -> 2
        "Low" -> 3
        else -> Int.MAX_VALUE
    }
}

@Composable
fun TaskItemCard(task: TaskEntity, navController: NavController) {
    val taskId = task.id
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                navController.navigate("${NavScreen.TaskDetails.route}/$taskId")
            }
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PurpleGrey80.copy(0.2f)
        ),
        border = BorderStroke(1.dp, color = MaterialTheme.customColors.buttonColor)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = task.title.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.customColors.blackWhite,
                fontWeight = FontWeight.Bold
            )
            CommonCardContent(
                "Date: ",
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(task.dueDate))
            )
            CommonCardContent("Priority: ", task.priority)
            CommonCardContent("Status: ", if (task.isCompleted) "Completed" else "Pending")
        }
    }
}

@Composable
fun CommonCardContent(title: String, description: String) {
    Row {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.customColors.blackWhite,
            fontWeight = FontWeight.Medium, fontSize = 18.sp
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.customColors.blackWhite,
            fontWeight = FontWeight.Normal, fontSize = 16.sp
        )
    }
}