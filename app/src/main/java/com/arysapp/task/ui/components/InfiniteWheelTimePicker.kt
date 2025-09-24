package com.arysapp.task.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun InfiniteWheelTimePicker(
    modifier: Modifier = Modifier,
    initialHour: Int = 0,
    initialMinute: Int = 0,
    minHour: Int = 0,
    minMinute: Int = 0,
    itemHeight: Dp = 48.dp,
    onTimeSelected: (hour: Int, minute: Int, dayIncrement: Boolean) -> Unit
) {
    var hour by remember { mutableIntStateOf(initialHour.coerceAtLeast(minHour)) }
    var minute by remember { mutableIntStateOf(
        if (hour == minHour) initialMinute.coerceAtLeast(minMinute) else initialMinute.coerceIn(0..59)
    ) }
    var dayIncrement by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InfiniteWheelColumn(
            items = (minHour..23).toList(),
            initialIndex = hour - minHour,
            width = 96.dp,
            itemHeight = itemHeight
        ) { selectedHour ->
            if (selectedHour < hour) {
                // اگر ساعت کمتر شد، یعنی روز به عقب نرود فقط مقدار ساعت را روی حداقل تنظیم می‌کنیم
                hour = if (selectedHour < minHour) {
                    minHour
                } else {
                    selectedHour
                }
                dayIncrement = false
            } else if (selectedHour > hour) {
                // اگر ساعت افزایش یافت ولی به 24 نرسید روز تغییر نکند
                hour = selectedHour
                dayIncrement = false
            } else {
                hour = selectedHour
                dayIncrement = false
            }

            // با تغییر ساعت، دقیقه را بر اساس minMinute اصلاح کن
            minute = if (hour == minHour) minute.coerceAtLeast(minMinute) else minute.coerceIn(0..59)
            onTimeSelected(hour, minute, dayIncrement)
        }

        Spacer(modifier = Modifier.width(4.dp))

        InfiniteWheelColumn(
            items = if (hour == minHour) (minMinute..59).toList() else (0..59).toList(),
            initialIndex = if (hour == minHour) minute - minMinute else minute,
            width = 96.dp,
            itemHeight = itemHeight
        ) { selectedMinute ->
            // زمانی که دقیقه به 0 رسید و ساعت 23 است، روز جلو می‌رود
            dayIncrement = hour == 23 && selectedMinute < minute

            minute = selectedMinute
            onTimeSelected(hour, minute, dayIncrement)
        }
    }
}

