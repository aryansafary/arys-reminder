package com.arysapp.task.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arysapp.task.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.task.utils.helper.JalaliDate
import kotlinx.coroutines.delay
import java.util.Calendar
import com.arysapp.task.R
import com.arysapp.task.utils.Constants.USER_LANGUAGE


@Composable
fun rememberCurrentTimeState(intervalMillis: Long = 60_000L): State<Calendar> {
    return produceState(initialValue = Calendar.getInstance()) {
        while (true) {
            value = Calendar.getInstance()
            delay(intervalMillis)
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun MyTimePicker(
    selectedDate: String,
    onTimeSelected: (String) -> Unit
) {
    val nowCalendar by rememberCurrentTimeState(30_000L)

    val todayYear = nowCalendar.get(Calendar.YEAR).toString()
    val todayMonth = (nowCalendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
    val todayDay = nowCalendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')

    val todayYearJalali = JalaliDate.today().year.toString()
    val todayMonthJalali = JalaliDate.today().month.toString().padStart(2, '0')
    val todayDayJalali = JalaliDate.today().day.toString().padStart(2, '0')

    val nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY)
    val nowMinute = nowCalendar.get(Calendar.MINUTE)

    var selectedHour by remember { mutableIntStateOf(nowHour) }
    var selectedMinute by remember { mutableIntStateOf((nowMinute + 1) % 60) }

    val parts = selectedDate.split("-")
    val isToday = if (parts.size == 3) {
        if (USER_LANGUAGE == PERSIAN_LANGUAGE) {
            parts[0].trim() == todayYearJalali &&
                    parts[1].trim() == todayMonthJalali &&
                    parts[2].trim() == todayDayJalali
        } else {
            parts[0].trim() == todayYear &&
                    parts[1].trim() == todayMonth &&
                    parts[2].trim() == todayDay
        }
    } else false

    LaunchedEffect(nowCalendar, isToday) {
        if (isToday) {
            if (selectedHour < nowHour || (selectedHour == nowHour && selectedMinute <= nowMinute)) {
                selectedHour = nowHour
                selectedMinute = (nowMinute + 1) % 60
                if (selectedMinute == 0) selectedHour++
                onTimeSelected(String.format("%02d:%02d", selectedHour, selectedMinute))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = stringResource(R.string.select_hour),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )


        LazyRow {
            items(24) { hour ->
                val disabled = isToday && hour < nowHour
                val selected = hour == selectedHour

                val bgColor by animateColorAsState(
                    targetValue = when {
                        disabled -> MaterialTheme.colorScheme.surfaceVariant
                        selected -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.surface
                    },
                    label = ""
                )

                val textColor by animateColorAsState(
                    targetValue = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    label = ""
                )

                val elevation by animateDpAsState(
                    targetValue = if (selected) 6.dp else 0.dp,
                    label = ""
                )

                Card(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(56.dp)
                        .clickable(enabled = !disabled) {
                            selectedHour = hour
                            if (isToday && hour == nowHour && selectedMinute <= nowMinute) {
                                selectedMinute = nowMinute + 1
                            }
                            onTimeSelected(String.format("%02d:%02d", selectedHour, selectedMinute))
                        },
                    colors = CardDefaults.cardColors(containerColor = bgColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = elevation),
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = String.format("%02d", hour),
                            color = textColor,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.select_minutes),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow {
            items(60) { minute ->
                val disabled = isToday && selectedHour == nowHour && minute <= nowMinute
                val selected = minute == selectedMinute

                val bgColor by animateColorAsState(
                    targetValue = when {
                        disabled -> MaterialTheme.colorScheme.surfaceVariant
                        selected -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.surface
                    },
                    label = ""
                )

                val textColor by animateColorAsState(
                    targetValue = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface,
                    label = ""
                )

                val elevation by animateDpAsState(
                    targetValue = if (selected) 6.dp else 0.dp,
                    label = ""
                )

                Card(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(56.dp)
                        .clickable(enabled = !disabled) {
                            selectedMinute = minute
                            onTimeSelected(String.format("%02d:%02d", selectedHour, selectedMinute))
                        },
                    colors = CardDefaults.cardColors(containerColor = bgColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = elevation),
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = String.format("%02d", minute),
                            color = textColor,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
