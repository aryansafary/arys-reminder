package com.arysapp.reminder.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arysapp.reminder.R
import com.arysapp.reminder.ui.theme.toDigits
import com.arysapp.reminder.utils.Constants.GREGORIAN_MONTHS
import com.arysapp.reminder.utils.Constants.GREGORIAN_WEEK_DAYS
import com.arysapp.reminder.utils.Constants.PERSIAN_WEEK_DAYS
import com.arysapp.reminder.utils.Constants.USER_LANGUAGE
import com.arysapp.reminder.utils.helper.JalaliDate
import com.arysapp.reminder.utils.helper.JalaliDate.Companion.getTodayDateString
import com.arysapp.reminder.utils.helper.JalaliDate.Companion.today
import java.util.Calendar
import kotlin.math.ceil

@Composable
fun MyCalendar(
    selectedDate: String,
    isPersian: Boolean,
    onDateSelected: (String) -> Unit,
    datesWithReminders: Set<String>,
    modifier: Modifier = Modifier
) {
    val initialParts = remember(selectedDate) {
        selectedDate.trim().split("-")
    }

    var currentViewYear by remember(isPersian) {
        mutableIntStateOf(
            initialParts.getOrNull(0)?.toIntOrNull()
                ?: if (isPersian) today().year else Calendar.getInstance().get(Calendar.YEAR)
        )
    }

    var currentViewMonth by remember(isPersian) {
        mutableIntStateOf(
            initialParts.getOrNull(1)?.toIntOrNull()
                ?: if (isPersian) today().month else (Calendar.getInstance().get(Calendar.MONTH) + 1)
        )
    }

    val onNextMonth: () -> Unit = remember {
        {
            if (currentViewMonth == 12) {
                currentViewMonth = 1
                currentViewYear++
            } else {
                currentViewMonth++
            }
        }
    }

    val onPrevMonth: () -> Unit = remember {
        {
            if (currentViewMonth == 1) {
                currentViewMonth = 12
                currentViewYear--
            } else {
                currentViewMonth--
            }
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            CalendarHeader(
                isPersian = isPersian,
                month = currentViewMonth,
                year = currentViewYear,
                onNextMonth = onNextMonth,
                onPrevMonth = onPrevMonth
            )

            Spacer(modifier = Modifier.height(8.dp))

            CalendarGrid(
                isPersian = isPersian,
                viewYear = currentViewYear,
                viewMonth = currentViewMonth,
                selectedDate = selectedDate.trim(),
                datesWithReminders = datesWithReminders,
                onDateSelected = onDateSelected
            )
        }
    }
}

@Composable
private fun CalendarHeader(
    isPersian: Boolean,
    month: Int,
    year: Int,
    onNextMonth: () -> Unit,
    onPrevMonth: () -> Unit
) {
    val monthName = if (isPersian) JalaliDate.monthNames[month - 1] else GREGORIAN_MONTHS[month - 1]
    val titleText by remember(monthName, year, isPersian) {
        derivedStateOf { "$monthName $year".toDigits(isDate = false, language = USER_LANGUAGE) }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = if (isPersian) onNextMonth else onPrevMonth,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(if (isPersian) R.drawable.icon_arrow_100px else R.drawable.icon_arrow_100px_2),
                contentDescription = "Right",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = titleText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        IconButton(
            onClick = if (isPersian) onPrevMonth else onNextMonth,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(if (isPersian) R.drawable.icon_arrow_100px_2 else R.drawable.icon_arrow_100px),
                contentDescription = "Left",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    isPersian: Boolean,
    viewYear: Int,
    viewMonth: Int,
    selectedDate: String,
    datesWithReminders: Set<String>,
    onDateSelected: (String) -> Unit
) {
    val weekDays = if (isPersian) PERSIAN_WEEK_DAYS else GREGORIAN_WEEK_DAYS
    val todayStr = remember(isPersian) { getTodayDateString(isPersian) }
    val (daysInMonth, startDayOfWeek) = remember(viewYear, viewMonth, isPersian) {
        if (isPersian) {
            val jDate = JalaliDate(viewYear, viewMonth, 1)
            Pair(jDate.getDaysInJalaliMonth(viewYear, viewMonth), jDate.getDayOfWeek())
        } else {
            val cal = Calendar.getInstance().apply {
                set(Calendar.YEAR, viewYear)
                set(Calendar.MONTH, viewMonth - 1)
                set(Calendar.DAY_OF_MONTH, 1)
            }
            Pair(cal.getActualMaximum(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_WEEK) - 1)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weekDays.forEach { day ->
                Text(
                    text = day.toDigits(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        val totalCells = daysInMonth + startDayOfWeek
        val rows = ceil(totalCells / 7.0).toInt()

        for (row in 0 until rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (col in 0 until 7) {
                    val cellIndex = (row * 7) + col
                    val dayNumber = cellIndex - startDayOfWeek + 1

                    if (cellIndex >= startDayOfWeek && dayNumber <= daysInMonth) {
                        val currentDateStr = "$viewYear-${
                            viewMonth.toString().padStart(2, '0')
                        }-${dayNumber.toString().padStart(2, '0')}"
                        val isPastDate = currentDateStr < todayStr
                        CalendarCell(
                            dateText = dayNumber.toString()
                                .toDigits(isDate = false, language = USER_LANGUAGE),
                            currentDateStr = currentDateStr,
                            isSelected = (currentDateStr == selectedDate),
                            isToday = (currentDateStr == todayStr),
                            hasReminder = datesWithReminders.contains(currentDateStr),
                            onDateClick = {
                                if (!isPastDate) {
                                    onDateSelected(it)
                                }
                            },
                            isPast = isPastDate,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarCell(
    dateText: String,
    currentDateStr: String,
    isSelected: Boolean,
    isToday: Boolean,
    hasReminder: Boolean,
    onDateClick: (String) -> Unit,
    isPast: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(36.dp)
            .clickable { onDateClick(currentDateStr) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Transparent)
                .then(
                    if (isToday && !isSelected) Modifier.border(
                        BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary),
                        RoundedCornerShape(8.dp)
                    ) else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dateText,
                color =
                    if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else if (isPast) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
            )
        }

        if (hasReminder) {
            Box(
                modifier = Modifier
                    .padding(top = 1.dp)
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary)
            )
        } else {
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}