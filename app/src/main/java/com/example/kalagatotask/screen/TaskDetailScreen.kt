package com.example.kalagatotask.screen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kalagatotask.R
import com.example.kalagatotask.component.CommonAlertDialog
import com.example.kalagatotask.component.CommonEditText
import com.example.kalagatotask.component.CommonEditTextWithDropdown
import com.example.kalagatotask.component.CommonHeadingText
import com.example.kalagatotask.component.CommonScreenHeading
import com.example.kalagatotask.navigation.NavScreen
import com.example.kalagatotask.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun TaskDetailScreen(navController: NavController, taskId: Long) {
    val viewModel: TaskViewModel = viewModel()
    val task by viewModel.getTaskById(taskId).collectAsState(initial = null)
    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    var showDialogDelete = remember { mutableStateOf(false) }

    var newDueDate by remember { mutableStateOf("") }
    var newStatus by remember { mutableStateOf(task?.isCompleted ?: false) }
    var titleState by remember { mutableStateOf("") }
    var descriptionState by remember { mutableStateOf("") }
    var priorityState by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            calendar.set(selectedYear, selectedMonth, selectedDay)
            newDueDate = dateFormatter.format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(task) {
        titleState = task?.title ?: ""
        descriptionState = task?.description ?: ""
        priorityState = task?.priority ?: ""
        newDueDate = task?.dueDate?.let { dateFormatter.format(Date(it)) } ?: ""
    }


    Scaffold(
        topBar = {
            CommonScreenHeading(
                "Task Details",
                navController,
                isSettingIcon = Icons.Default.Delete,
                isSettingIconVisible = true,
                isSettingIconClick = {
                    showDialogDelete.value = true
                }
            )
        },
        bottomBar = {
            Button(  modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 30.dp),  onClick = {
                val updatedTask = task?.copy(
                    title = titleState,
                    priority = priorityState,
                    description = descriptionState,
                    isCompleted = newStatus,
                    dueDate = calendar.timeInMillis
                )

                if (updatedTask != null) {
                    viewModel.updateTask(updatedTask)
                    Toast.makeText(context, "Task updated", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }) {
                Text("Update Task", color = Color.White)
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CommonHeadingText("Title")
            CommonEditText(
                value = titleState,
                onValueChange = { newTitle ->
                    titleState = newTitle
                },
                enabled = true,
                hintText = "Please enter title"
            )

            CommonHeadingText("Description")
            CommonEditText(
                value = descriptionState,
                onValueChange = { newDescription ->
                    descriptionState = newDescription
                },
                enabled = true,
                hintText = "Please enter description"
            )

            CommonHeadingText("Priority")
            CommonEditTextWithDropdown(
                value = priorityState,
                onValueChange = { newPriority ->
                    priorityState = newPriority
                },
                enabled = false,
                hintText = "Please select priority",
                dropdownItems = listOf("High", "Medium", "Low")
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Status: ")
                Switch(
                    checked = newStatus,
                    onCheckedChange = { newStatus = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Green,
                        uncheckedThumbColor = Color.Red
                    )
                )
                Text(text = if (newStatus) " Completed" else " Pending")
            }
            CommonHeadingText("Due Date")
            CommonEditText(
                value = newDueDate,
                onValueChange = { newDueDate = it },
                enabled = false,
                hintText = "Please enter date",
                modifier = Modifier.clickable { datePickerDialog.show() },
                trailingIcon = {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "",
                        modifier = Modifier.clickable { datePickerDialog.show() }
                    )
                }
            )
        }
    }
    if (showDialogDelete.value) {
        CommonAlertDialog(showDialogDelete, onYesClick = {
            task?.let { viewModel.deleteTask(it) }
            navController.navigate(NavScreen.Home.route)
        })
    }
}


