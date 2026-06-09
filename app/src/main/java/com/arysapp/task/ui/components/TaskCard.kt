package com.arysapp.task.ui.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.arysapp.task.R
import com.arysapp.task.domain.model.RepeatType
import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.ui.theme.toDigits
import com.arysapp.task.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.task.utils.Constants.USER_LANGUAGE

@Composable
fun TaskCard(
    task: TaskModel,
    onEdit: (TaskModel) -> Unit,
    onDelete: (TaskModel) -> Unit,
    onToggle: (Boolean) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    val cardColor = if (task.isActive) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.secondary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = { onEdit(task) },
                onLongClick = { showDeleteDialog = true }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(if (task.isActive) 8.dp else 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title + Repeat Type Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                AssistChip(
                    onClick = {  },
                    label = {
                        val repeatTypeEnum = RepeatType.valueOf(task.repeatType)
                        Text(
                            text = repeatTypeEnum.getLabel(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.repeat_100px_1),
                            contentDescription = "Repeat",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.height(24.dp)
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            task.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.2.dp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
            )

            // Time Row with Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.calendar_icon),
                        contentDescription = "Date",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = task.dateTime.toString().toDigits(USER_LANGUAGE==PERSIAN_LANGUAGE),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(R.drawable.time_icon),
                        contentDescription = "Time",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text =
                            task.hourTime.toString().toDigits(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Switch(
                    checked = task.isActive,
                    onCheckedChange = { onToggle(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.secondary,
                        checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                        uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.delete_100px),
                        contentDescription = "Delete Task Icon",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp),


                )
            },
            title = { Text(text = stringResource(R.string.deleteTask),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
            ) },
            text = { Text(text = stringResource(R.string.delete_task_confirmation)) },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(task)
                }) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { }) {
                    Text(text = stringResource(R.string.no))
                }
            }
        )
    }
}
