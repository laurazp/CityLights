package com.luridevlabs.citylights.presentation.personallists.composables

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.model.MonumentList

@Composable
fun AddToPersonalListsDialog(
    onListClick: (MonumentList) -> Unit,
    onDismissClick: () -> Unit,
    dialogTitle: String,
    personalLists: List<MonumentList>
) {
    val context = LocalContext.current

    val shouldDismiss = remember {
        mutableStateOf(false)
    }

    if (shouldDismiss.value) return

    AlertDialog(
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_favorite_24),
                contentDescription = stringResource(R.string.add),
                tint = MaterialTheme.colorScheme.secondary
            )
        },
        title = {
            Text(text = dialogTitle,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        text = {
            Column {
                personalLists.forEach { list ->
                    Row(
                        Modifier
                            .clickable(onClick = {
                                onListClick(list)
                                shouldDismiss.value = true
                            })
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(20.dp))

                        Text(text = list.listName,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        },
        onDismissRequest = {
            onDismissClick()
            shouldDismiss.value = true
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissClick()
                    shouldDismiss.value = true
                }
            ) {
                Text(stringResource(R.string.cancelButtonText))
            }
        }
    )
}





    /*
    expanded:Boolean,
    lists: List<String>,
    onDismiss: () -> Unit,
    onListClick: (String) -> Unit,
    onAddListClick: () -> Unit,
    dialogTitle: String

    /*onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String*/
) {
    val shouldDismiss = remember {
        mutableStateOf(false)
    }

    if (shouldDismiss.value) return

    AlertDialog(onDismissRequest = {
        onDismiss
        //shouldDismiss.value = true
    }) {
        Column {
            Text(text = dialogTitle)

            Spacer(Modifier.height(20.dp))

            lists.forEach { list ->
                Row(
                    Modifier
                        .clickable(onClick = { onListClick(list) })
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(20.dp))

                    Text(text = list)
                }
            }

            Row(
                modifier = Modifier
                    .clickable(onClick = onAddListClick)
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color = Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(24.dp),
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Opción para añadir cuenta"
                    )
                }
                Spacer(Modifier.width(20.dp))
                Text(text = "Añadir a lista")
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}*/