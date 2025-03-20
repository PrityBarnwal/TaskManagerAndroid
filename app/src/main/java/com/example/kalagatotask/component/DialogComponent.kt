package com.example.kalagatotask.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun CommonAlertDialog(showDialogDelete: MutableState<Boolean>, onYesClick: () -> Unit){
    AlertDialog(
        onDismissRequest = {
            showDialogDelete.value = false
        },
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete all tasks?") },
        confirmButton = {
            TextButton(onClick = {
                onYesClick.invoke()
                showDialogDelete.value = false
            }) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                showDialogDelete.value = false
            }) {
                Text("Cancel")
            }
        }
    )
}