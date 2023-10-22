package com.luridevlabs.citylights.presentation.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.luridevlabs.citylights.R

@Composable
fun ErrorAlertDialog(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_error_outline_24),
                contentDescription = "Error Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {}
            ) {
                Text("Dismiss")
            }
        }
    )
}