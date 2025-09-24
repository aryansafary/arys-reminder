package com.arysapp.task.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arysapp.task.utils.helper.JalaliDate
@Composable
fun InfiniteWheelJalaliDatePicker(
    modifier: Modifier = Modifier,
    initialDate: JalaliDate = JalaliDate.today(),
    minDate: JalaliDate = JalaliDate.today(),
    maxYearOffset: Int = 7,
    itemHeight: Dp = 48.dp,
    onDateSelected: (JalaliDate) -> Unit
) {
    val minYear = minDate.year
    val maxYear = minYear + maxYearOffset

    var selectedYear by remember { mutableIntStateOf(initialDate.year.coerceIn(minYear, maxYear)) }
    var selectedMonth by remember { mutableIntStateOf(initialDate.month.coerceIn(1, 12)) }
    val daysInMonth = remember(selectedYear, selectedMonth) {
        JalaliDate(selectedYear, selectedMonth, 1).getDaysInJalaliMonth(selectedYear, selectedMonth)
    }
    val minSelectableDay = if (selectedYear == minDate.year && selectedMonth == minDate.month) minDate.day else 1
    var selectedDay by remember {
        mutableIntStateOf(initialDate.day.coerceIn(minSelectableDay, daysInMonth))
    }

    val years = (minYear..maxYear).toList()
    val months = (1..12).toList()
    val days = (minSelectableDay..daysInMonth).toList()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfiniteWheelColumn(
            items = years,
            initialIndex = years.indexOf(selectedYear),
            width = 96.dp,
            itemHeight = itemHeight,
            formatter = { it.toString() }
        ) { y ->
            selectedYear = y
            val newDaysInMonth = JalaliDate(y, selectedMonth, 1).getDaysInJalaliMonth(y, selectedMonth)
            val newMinDay = if (y == minDate.year && selectedMonth == minDate.month) minDate.day else 1
            selectedDay = selectedDay.coerceIn(newMinDay, newDaysInMonth)
            onDateSelected(JalaliDate(selectedYear, selectedMonth, selectedDay))
        }

        Spacer(modifier = Modifier.width(4.dp))

        InfiniteWheelColumn(
            items = months,
            initialIndex = selectedMonth - 1,
            width = 96.dp,
            itemHeight = itemHeight,
            formatter = { JalaliDate.monthNames[it - 1] }
        ) { m ->
            selectedMonth = m
            val newDaysInMonth = JalaliDate(selectedYear, m, 1).getDaysInJalaliMonth(selectedYear, m)
            val newMinDay = if (selectedYear == minDate.year && m == minDate.month) minDate.day else 1
            selectedDay = selectedDay.coerceIn(newMinDay, newDaysInMonth)
            onDateSelected(JalaliDate(selectedYear, selectedMonth, selectedDay))
        }

        Spacer(modifier = Modifier.width(4.dp))

        InfiniteWheelColumn(
            items = days,
            initialIndex = days.indexOf(selectedDay),
            width = 96.dp,
            itemHeight = itemHeight,
            formatter = { it.toString().padStart(2, '0') }
        ) { d ->
            selectedDay = d
            onDateSelected(JalaliDate(selectedYear, selectedMonth, selectedDay))
        }
    }
}
