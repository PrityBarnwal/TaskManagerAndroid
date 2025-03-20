package com.example.kalagatotask.screen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kalagatotask.ui.theme.customColors
import com.example.kalagatotask.component.CommonEditText
import com.example.kalagatotask.component.CommonEditTextWithDropdown
import com.example.kalagatotask.component.CommonHeadingText
import com.example.kalagatotask.component.CommonScreenHeading
import com.example.kalagatotask.room.TaskEntity
import com.example.kalagatotask.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AddTaskScreen(navController: NavController) {
    val viewModel: TaskViewModel = viewModel()
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val priority = remember { mutableStateOf("") }
    val dueDate = remember { mutableStateOf("") }
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            dueDate.value = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        },
        year,
        month,
        day
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            CommonScreenHeading("Create Task", navController)
            CommonHeadingText("Title")
            CommonEditText(
                value = title.value,
                onValueChange = { title.value = it },
                enabled = true,
                hintText = "Please enter title"
            )

            CommonHeadingText("Description")
            CommonEditText(
                value = description.value,
                onValueChange = { description.value = it },
                isSingleLine = false,
                enabled = true,
                hintText = "Please enter description"
            )

            CommonHeadingText("Priority")
            CommonEditTextWithDropdown(
                value = priority.value,
                onValueChange = { priority.value = it },
                enabled = false,
                hintText = "Please select priority",
                dropdownItems = listOf("High", "Medium", "Low"),
            )

            CommonHeadingText("Due Date")
            CommonEditText(
                value = dueDate.value,
                onValueChange = { dueDate.value = it },
                enabled = false,
                hintText = "Please enter date",
                modifier = Modifier.clickable {
                    datePickerDialog.show()
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            datePickerDialog.show()
                        })
                })
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 30.dp),
            onClick = {
                // Validate fields
                if (title.value.isEmpty()) {
                    Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (description.value.isEmpty()) {
                    Toast.makeText(context, "Description cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                    return@Button
                }
                if (priority.value.isEmpty()) {
                    Toast.makeText(context, "Priority cannot be empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (dueDate.value.isEmpty()) {
                    Toast.makeText(context, "Due Date cannot be empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                try {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = dateFormat.parse(dueDate.value)
                    val timestamp = date?.time ?: System.currentTimeMillis()

                    val task = TaskEntity(
                        title = title.value,
                        description = description.value,
                        priority = priority.value,
                        dueDate = timestamp
                    )
                    viewModel.addTask(task)
                    navController.popBackStack()
                } catch (e: Exception) {
                    Toast.makeText(context, "Invalid date format", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.customColors.buttonColor)
        ) {
            Text("Save Task", color = Color.White)
        }
    }
}


//@Composable
//fun AddTaskScreen(navController: NavController) {
//    val viewModel: TaskViewModel = viewModel()
//    val title = remember { mutableStateOf("") }
//    val description = remember { mutableStateOf("") }
//    val priority = remember { mutableStateOf("") }
//    val dueDate = remember { mutableStateOf("") }
//    val context = LocalContext.current
//
//    val calendar = Calendar.getInstance()
//    val year = calendar.get(Calendar.YEAR)
//    val month = calendar.get(Calendar.MONTH)
//    val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//    val datePickerDialog = DatePickerDialog(
//        context,
//        { _, selectedYear, selectedMonth, selectedDay ->
//            dueDate.value = "$selectedDay/${selectedMonth + 1}/$selectedYear"
//        },
//        year,
//        month,
//        day
//    )
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//            CommonScreenHeading("Create Task", navController)
//            CommonHeadingText("Title")
//            CommonEditText(
//                value = title.value,
//                onValueChange = { title.value = it },
//                enabled = true,
//                hintText = "Please enter title"
//            )
//
//            CommonHeadingText("Description")
//            CommonEditText(
//                value = description.value,
//                onValueChange = { description.value = it },
//                isSingleLine = false,
//                enabled = true,
//                hintText = "Please enter description"
//            )
//
//            CommonHeadingText("Priority")
//            CommonEditTextWithDropdown(
//                value = priority.value,
//                onValueChange = {
//                    priority.value = it
//                },
//                enabled = true,
//                hintText = "Please select priority",
//                dropdownItems = listOf("High", "Medium", "Low")
//            )
//
//            CommonHeadingText("Due Date")
//            CommonEditText(
//                value = dueDate.value,
//                onValueChange = { dueDate.value = it },
//                enabled = false,
//                hintText = "Please enter date",
//                modifier = Modifier.clickable {
//                    datePickerDialog.show()
//                },
//                trailingIcon = {
//                    Icon(
//                        Icons.Default.ArrowDropDown,
//                        contentDescription = "",
//                        modifier = Modifier.clickable {
//                            datePickerDialog.show()
//                        })
//                })
//        }
//
//        Button(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//                .padding(horizontal = 20.dp, vertical = 30.dp), onClick = {
//                try {
//                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//                    val date = dateFormat.parse(dueDate.value)
//                    val timestamp = date?.time ?: System.currentTimeMillis()
//
//                    val task = TaskEntity(
//                        title = title.value,
//                        description = description.value,
//                        priority = priority.value,
//                        dueDate = timestamp
//                    )
//                    viewModel.addTask(task)
//                    navController.popBackStack()
//                } catch (e: Exception) {
//                    Toast.makeText(context, "Invalid date format", Toast.LENGTH_SHORT).show()
//                }
//            }, colors = ButtonDefaults.buttonColors(MaterialTheme.customColors.buttonColor)
//        ) {
//            Text("Save Task", color = Color.White)
//        }
//    }
//}
