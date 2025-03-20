package com.example.kalagatotask.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kalagatotask.ui.theme.customColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonScreenHeading(
    content: String,
    navController: NavController,
    isIconVisible: Boolean = true,
    isSettingIconVisible: Boolean = false,
    isSettingIcon: ImageVector = Icons.Default.Settings,
    isSettingIconClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = content,
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = {
            if (isIconVisible) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }

        },
        actions = {
            if (isSettingIconVisible) {
                IconButton(onClick = {
                    isSettingIconClick.invoke()
                }) {
                    Icon(
                        isSettingIcon,
                        contentDescription = "Settings",
                        tint = MaterialTheme.customColors.blackWhite
                    )
                }
            }
        },

        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun CommonHeadingText(content: String) {
    Text(
        content,
        color = MaterialTheme.customColors.blackWhite,
        fontSize = 14.sp
    )
}

@Composable
fun CommonEditText(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    hintText: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = hintText, color = MaterialTheme.customColors.blackWhite) },
        singleLine = isSingleLine,
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.customColors.blackWhite
        ),
        modifier = modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.customColors.blackWhite,
            unfocusedBorderColor = MaterialTheme.customColors.grayLightGray,
            cursorColor = MaterialTheme.customColors.blackWhite,
            focusedLabelColor = MaterialTheme.customColors.blackWhite,
            unfocusedLabelColor = MaterialTheme.customColors.blackWhite
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonEditTextWithDropdown(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    hintText: String,
    dropdownItems: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val dismissKeyboard = {
        keyboardController?.hide()
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = hintText, color = MaterialTheme.colorScheme.onBackground) },
            textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
            modifier = modifier
                .menuAnchor()
                .padding(bottom = 10.dp)
                .fillMaxWidth()
                .clickable {

                    dismissKeyboard()
                },
            enabled = enabled,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            dropdownItems.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item, color = MaterialTheme.customColors.blackWhite) },
                    onClick = {
                        onValueChange(item)
                        expanded = false
                    }
                )
                if (index < dropdownItems.size - 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(color = MaterialTheme.customColors.grayLightGray)
                    )
                }
            }
        }
    }
}