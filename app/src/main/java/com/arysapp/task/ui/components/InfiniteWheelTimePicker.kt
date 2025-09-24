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
    itemHeight: Dp = 48.dp,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    var hour by remember { mutableIntStateOf(initialHour.coerceIn(0..23)) }
    var minute by remember { mutableIntStateOf(initialMinute.coerceIn(0..59)) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InfiniteWheelColumn(
            items = (0..23).toList(),
            initialIndex = hour,
            width = 96.dp,
            itemHeight = itemHeight
        ) { selectedHour ->
            hour = selectedHour
            onTimeSelected(hour, minute)
        }

        Spacer(modifier = Modifier.width(4.dp))

        InfiniteWheelColumn(
            items = (0..59).toList(),
            initialIndex = minute,
            width = 96.dp,
            itemHeight = itemHeight
        ) { selectedMinute ->
            minute = selectedMinute
            onTimeSelected(hour, minute)
        }
    }
}


