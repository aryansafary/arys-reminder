package com.arysapp.reminder.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arysapp.reminder.R
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.ui.components.MyDatePicker
import com.arysapp.reminder.ui.components.MyOutlinedTextField
import com.arysapp.reminder.ui.components.MyTimePicker
import com.arysapp.reminder.ui.model.ReminderCategory
import com.arysapp.reminder.ui.theme.toDigits
import com.arysapp.reminder.ui.viewmodel.ReminderViewModel
import com.arysapp.reminder.utils.Constants.ENGLISH_LANGUAGE
import com.arysapp.reminder.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.reminder.utils.Constants.USER_LANGUAGE
import com.arysapp.reminder.utils.helper.JalaliDate
import com.arysapp.reminder.utils.helper.JalaliDate.Companion.isDateTimeInFuture
import java.util.Calendar
import java.util.Locale

@Composable
fun AddReminderScreen(
    reminderViewModel: ReminderViewModel = hiltViewModel(),
    navController: NavController,
    reminderModel: ReminderModel? = null
) {
    val isEditMode = reminderModel != null
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()

    var title by remember(reminderModel) { mutableStateOf(reminderModel?.title ?: "") }
    var description by remember(reminderModel) { mutableStateOf(reminderModel?.description ?: "") }
    var repeatType by remember(reminderModel) { mutableStateOf(reminderModel?.repeatType ?: RepeatType.NONE.name) }
    var categoryKey by remember(reminderModel) { mutableStateOf(reminderModel?.category ?: ReminderCategory.General.key) }
    var time by remember(reminderModel) {
        mutableStateOf(
            reminderModel?.hourTime ?: String.format(
                Locale.ENGLISH,
                "%02d:%02d",
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE)
            )
        )
    }

    var date by remember(reminderModel) {
        mutableStateOf(
            reminderModel?.dateTime?.toDigits(USER_LANGUAGE==PERSIAN_LANGUAGE) ?: if (USER_LANGUAGE == PERSIAN_LANGUAGE) {
                val jDate = JalaliDate.today()
                "${jDate.year}-${jDate.month.toString().padStart(2, '0')}-${jDate.day.toString().padStart(2, '0')}"
            } else {
                val cal = Calendar.getInstance()
                "${cal.get(Calendar.YEAR)}-${(cal.get(Calendar.MONTH) + 1).toString().padStart(2, '0')}-${cal.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')}"
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                MyOutlinedTextField(
                    value = title,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { title = it },
                    label = context.getString(R.string.title),
                    maxLength = 22,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.title_100px_1),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                )

                MyOutlinedTextField(
                    value = description,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { description = it },
                    label = context.getString(R.string.description),
                    maxLength = 50,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.description_100px_2),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SectionHeader(
                        icon = painterResource(R.drawable.folder),
                        title = stringResource(R.string.category)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(ReminderCategory.getAllCategories()) { category ->
                            val isSelected = categoryKey == category.key
                            val categoryColor = if (isDarkTheme) category.darkColor else category.lightColor

                            val chipBgColor by animateColorAsState(
                                targetValue = if (isSelected) categoryColor else MaterialTheme.colorScheme.surface,
                                animationSpec = tween(200),
                                label = "chipBgColor"
                            )

                            val contentColor by animateColorAsState(
                                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                animationSpec = tween(200),
                                label = "contentColor"
                            )

                            FilterChip(
                                selected = isSelected,
                                onClick = { categoryKey = category.key },
                                label = {
                                    Text(
                                        text = stringResource(category.titleResId),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(category.iconResId),
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = if (isSelected) contentColor else categoryColor
                                    )
                                },
                                shape = RoundedCornerShape(16.dp),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outlineVariant
                                ),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = chipBgColor,
                                    selectedLabelColor = contentColor,
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        MyDatePicker(
                            selectedDate = date,
                            modifier = Modifier.fillMaxWidth()
                        ) { newDate ->
                            date = newDate
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        MyTimePicker(
                            selectedDate = date,
                            modifier = Modifier.fillMaxWidth()
                        ) { newTime ->
                            time = newTime
                        }
                    }
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SectionHeader(
                        icon = painterResource(R.drawable.repeat_icon),
                        title = context.getString(R.string.select_repeatType)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(RepeatType.entries) { type ->
                            val isSelected = repeatType == type.name
                            val chipBg by animateColorAsState(
                                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                label = "repeatChipBg"
                            )
                            val chipContent by animateColorAsState(
                                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                label = "repeatChipContent"
                            )

                            AssistChip(
                                onClick = { repeatType = type.name },
                                label = {
                                    Text(
                                        text = type.getLabel(),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                shape = RoundedCornerShape(16.dp),
                                border = AssistChipDefaults.assistChipBorder(
                                    enabled = true,
                                    borderColor = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outlineVariant
                                ),
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = chipBg,
                                    labelColor = chipContent
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        val isFormValid = title.isNotBlank() &&
                date.isNotBlank() &&
                time.isNotBlank() &&
                repeatType.isNotBlank() &&
                isDateTimeInFuture(date.toDigits(USER_LANGUAGE == PERSIAN_LANGUAGE, ENGLISH_LANGUAGE), time)

        Button(
            enabled = isFormValid,
            onClick = {
                val dateTime = date.toDigits(USER_LANGUAGE == PERSIAN_LANGUAGE, ENGLISH_LANGUAGE)
                var days: Int? = null
                var weeks: Int? = null
                var months: Int? = null
                var years: Int? = null

                when (repeatType) {
                    RepeatType.DAILY.name -> days = 1
                    RepeatType.WEEKLY.name -> weeks = 1
                    RepeatType.MONTHLY.name -> months = 1
                    RepeatType.YEARLY.name -> years = 1
                }

                if (isEditMode && reminderModel != null) {
                    reminderViewModel.updateReminder(
                        reminderModel.copy(
                            title = title,
                            description = description,
                            dateTime = dateTime,
                            hourTime = time,
                            repeatType = repeatType,
                            repeatIntervalDays = days,
                            repeatIntervalWeeks = weeks,
                            repeatIntervalMonths = months,
                            repeatIntervalYears = years,
                            category = categoryKey,
                            isActive = true
                        )
                    )
                } else {
                    reminderViewModel.insertReminder(
                        title = title,
                        description = description,
                        dateTime = dateTime,
                        hourTime = time,
                        repeatType = repeatType,
                        repeatIntervalDays = days,
                        repeatIntervalWeeks = weeks,
                        repeatIntervalMonths = months,
                        repeatIntervalYears = years,
                        category = categoryKey
                    )
                }
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .height(60.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = if (isFormValid) 4.dp else 0.dp,
                pressedElevation = 1.dp
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.add_reminder_2),
                contentDescription = "Save",
                tint = if (isFormValid) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(if (isEditMode) R.string.Edit_task else R.string.save_reminder),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SectionHeader(icon: Painter, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}