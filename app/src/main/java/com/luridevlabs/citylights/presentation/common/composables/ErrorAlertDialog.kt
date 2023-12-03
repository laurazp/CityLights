package com.luridevlabs.citylights.presentation.common.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.luridevlabs.citylights.R

@Composable
fun ErrorAlertDialog(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    val shouldDismiss = remember {
        mutableStateOf(false)
    }
    
    if (shouldDismiss.value) return

    AlertDialog(
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_error_outline_24),
                contentDescription = stringResource(R.string.error_icon)
            )
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            shouldDismiss.value = true
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                    shouldDismiss.value = true
                }
            ) {
                Text(stringResource(R.string.try_again))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    shouldDismiss.value = true
                }
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}
