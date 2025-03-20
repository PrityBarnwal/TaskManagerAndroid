package com.example.kalagatotask.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kalagatotask.ui.theme.customColors
import com.example.kalagatotask.component.CommonAlertDialog
import com.example.kalagatotask.component.CommonScreenHeading
import com.example.kalagatotask.navigation.NavScreen
import com.example.kalagatotask.viewmodel.TaskViewModel


@Composable
fun SettingScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    completedProgress: Float
) {
    val viewModel: TaskViewModel = viewModel()

    Column {
        CommonScreenHeading("Setting", navController, isSettingIconVisible = true)
        SettingsScreen(darkMode = isDarkTheme, onDarkModeToggle = {
            onThemeChange(it)
        }, viewModel = viewModel, navController, completedProgress)
    }
}


@Composable
fun SettingsScreen(
    darkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    viewModel: TaskViewModel,
    navController: NavController,
    completedProgress: Float
) {
    var showDialog = remember { mutableStateOf(false) }
    var deleteAllChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            ProgressIndicator(
                progress = completedProgress,
                percentage = (completedProgress * 100).toInt()
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode", modifier = Modifier.weight(1f))
            Switch(checked = darkMode, onCheckedChange = onDarkModeToggle)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Delete All Task", modifier = Modifier.weight(1f))
            Switch(checked = deleteAllChecked, onCheckedChange = {
                deleteAllChecked = true
                showDialog.value = true
            })
        }
    }

    if (showDialog.value) {
        CommonAlertDialog(showDialog, onYesClick = {
            viewModel.deleteAllTask()
            navController.navigate(NavScreen.Home.route)
        })
    }
}

@Composable
fun ProgressIndicator(progress: Float, percentage: Int) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000), label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(120.dp)
    ) {

        CircularProgressIndicator(
            progress = { 1f },
            color = MaterialTheme.customColors.buttonColor.copy(alpha = 0.2f),
            strokeWidth = 8.dp,
            modifier = Modifier.size(100.dp)
        )

        CircularProgressIndicator(
            progress = { animatedProgress },
            color = MaterialTheme.customColors.buttonColor,
            strokeWidth = 8.dp,
            modifier = Modifier.size(100.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$percentage%",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.customColors.buttonColor
            )
            Text(
                text = "Completed",
                fontSize = 12.sp,
                color = MaterialTheme.customColors.grayLightGray
            )
        }
    }
}
