package com.arysapp.task.ui.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arysapp.task.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.task.utils.helper.JalaliDate
import kotlinx.coroutines.delay
import java.util.Calendar
import com.arysapp.task.R


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
    userLanguage: String = PERSIAN_LANGUAGE,
    selectedDate: String,
    onTimeSelected: (String) -> Unit
) {
    val nowCalendar by rememberCurrentTimeState(30_000L)

    val todayYear = nowCalendar.get(Calendar.YEAR).toString()
    val intMonth = nowCalendar.get(Calendar.MONTH) + 1
    val todayMonth = intMonth.toString().padStart(2, '0')
    val todayDay = nowCalendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')


    val todayYearJalali = JalaliDate.today().year.toString()
    val todayMonthJalali = JalaliDate.today().month.toString().padStart(2, '0')
    val todayDayJalali = JalaliDate.today().day.toString().padStart(2, '0')

    val nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY)
    val nowMinute = nowCalendar.get(Calendar.MINUTE)

    var selectedHour by remember { mutableIntStateOf(nowHour) }
    var selectedMinute by remember { mutableIntStateOf((nowMinute + 1) % 60) }

    Log.d("Date", "Jalali:$todayYearJalali-$todayMonthJalali-$todayDayJalali \n Milad:$todayYear-$todayMonth-$todayDay ")

    val parts = selectedDate.split("-")
    val isToday = if (parts.size == 3) {
        if (userLanguage == PERSIAN_LANGUAGE) {
                    parts[0].trim() == todayYearJalali &&
                    parts[1].trim() == todayMonthJalali &&
                    parts[2].trim() == todayDayJalali
        } else {
                    parts[0].trim() == todayYear &&
                    parts[1].trim() == todayMonth &&
                    parts[2].trim() == todayDay
        }
    } else {
        false
    }

    Log.d("Date", "Today:${parts[0]}-${parts[1]}-${parts[2]} ")
    Log.d("DateCheck", "UserLang=$userLanguage  isToday=$isToday  selectedDate=$selectedDate \n parts=${parts.size}  parts=$parts\"" )
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

    Column {
        Text(text = stringResource(R.string.selecting_time))

        LazyRow {
            items(24) { hour ->
                val disabled = isToday && hour < nowHour
                val selected = hour == selectedHour
                Text(
                    text = String.format("%02d", hour),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(enabled = !disabled) {
                            selectedHour = hour
                            if (isToday && hour == nowHour && selectedMinute <= nowMinute) {
                                selectedMinute = nowMinute + 1
                            }
                            onTimeSelected(String.format("%02d:%02d", selectedHour, selectedMinute))
                        },
                    color = when {
                        disabled -> Color.Gray
                        selected -> Color.Red
                        else -> Color.Black
                    }
                )
            }
        }

        Spacer(Modifier.height(12.dp))


        LazyRow {
            items(60) { minute ->
                val disabled = isToday && selectedHour == nowHour && minute <= nowMinute
                val selected = minute == selectedMinute
                Text(
                    text = String.format("%02d", minute),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(enabled = !disabled) {
                            selectedMinute = minute
                            onTimeSelected(String.format("%02d:%02d", selectedHour, selectedMinute))
                        },
                    color = when {
                        disabled -> Color.Gray
                        selected -> Color.Red
                        else -> Color.Black
                    }
                )
            }
        }
    }
}