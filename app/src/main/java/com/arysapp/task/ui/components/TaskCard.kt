package com.arysapp.task.ui.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.R
import com.arysapp.task.domain.model.RepeatType
import com.arysapp.task.ui.theme.toPersianDigits
import com.arysapp.task.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.task.utils.Constants.USER_LANGUAGE



@Composable
fun TaskCard(
    task: TaskModel,
    onToggle: (Boolean) -> Unit,
) {
    val cardColor by animateColorAsState(
        if (task.isActive) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.secondary,
        label = "Card Color Animation"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
            // Title + Repeat Type
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )

                AssistChip(
                    onClick = { },
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

            // Time Row
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
                        text = if(USER_LANGUAGE==PERSIAN_LANGUAGE)task.dateTime.toString().toPersianDigits()else task.dateTime ?: "----/--/--",
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
                        text = if(USER_LANGUAGE==PERSIAN_LANGUAGE)task.hourTime.toString().toPersianDigits()else task.hourTime ?: "--:--",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                // Toggle Switch
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
}





