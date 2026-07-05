package com.arysapp.reminder.ui.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.arysapp.reminder.R
import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.ui.theme.toDigits
import com.arysapp.reminder.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.reminder.utils.Constants.USER_LANGUAGE

@Composable
fun ReminderCard(
    reminder: ReminderModel,
    onEdit: (ReminderModel) -> Unit,
    onDelete: (ReminderModel) -> Unit,
    onToggle: (Boolean) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    val cardColor = if (reminder.isActive) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.secondary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = { onEdit(reminder) },
                onLongClick = { showDeleteDialog = true }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(if (reminder.isActive) 8.dp else 2.dp),
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
                    text = reminder.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                AssistChip(
                    onClick = { },
                    label = {
                        val repeatTypeEnum = RepeatType.valueOf(reminder.repeatType)
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
            reminder.description?.let {
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
                Column(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TextWhitIcon(
                        icon = painterResource(R.drawable.calendar_icon),
                        text = reminder.dateTime.toString().toDigits(USER_LANGUAGE == PERSIAN_LANGUAGE)
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    TextWhitIcon(
                        icon = painterResource(R.drawable.time_icon),
                        text = reminder.hourTime.toString().toDigits()
                    )
                }
                Switch(
                    checked = reminder.isActive,
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
                    contentDescription = "Delete Reminder Icon",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp),


                    )
            },
            title = {
                Text(
                    text = stringResource(R.string.deleteReminder),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = { Text(text = stringResource(R.string.delete_reminder_confirmation)) },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(reminder)
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


@Composable
fun TextWhitIcon(
    text: String,
    icon: Painter
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
