package com.arysapp.reminder.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arysapp.reminder.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.reminder.utils.helper.JalaliDate
import kotlinx.coroutines.delay
import java.util.Calendar
import com.arysapp.reminder.R
import com.arysapp.reminder.ui.theme.toDigits
import com.arysapp.reminder.utils.Constants.USER_LANGUAGE
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun rememberCurrentTimeState(intervalMillis: Long = 60_000L): State<Calendar> {
    return produceState(initialValue = Calendar.getInstance()) {
        while (true) {
            value = Calendar.getInstance()
            delay(intervalMillis.milliseconds)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale")
@Composable
fun MyTimePicker(
    selectedDate: String,
    modifier: Modifier = Modifier,
    onTimeSelected: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    var currentSelectedTimeText by remember { mutableStateOf("") }

    val nowCalendar by rememberCurrentTimeState(1000L)
    val nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY)
    val nowMinute = nowCalendar.get(Calendar.MINUTE)

    var selectedHour by remember { mutableIntStateOf(nowHour) }
    var selectedMinute by remember { mutableIntStateOf((nowMinute + 1) % 60) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            //.padding(4.dp)
    ) {
        OutlinedButton(
            onClick = { showPicker = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp , bottom = 8.dp)

            ){
                Icon(
                painter = painterResource(R.drawable.time_icon),
                contentDescription = "time",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier= Modifier.size(24.dp)
            )
                Text(
                    text = if (currentSelectedTimeText.isNotEmpty()) currentSelectedTimeText.toDigits() else stringResource(R.string.select_hour),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 8.dp)
                )

            }

        }

        if (showPicker) {
            ModalBottomSheet(
                onDismissRequest = { showPicker = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                TimePickerBottomSheetContent(
                    selectedDate = selectedDate,
                    onDismiss = { showPicker = false },
                    onConfirm = { hour, minute ->
                        selectedHour = hour
                        selectedMinute = minute
                        val formattedTime = String.format("%02d:%02d", hour, minute)
                        currentSelectedTimeText = formattedTime
                        onTimeSelected(formattedTime)
                        showPicker = false
                    }
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TimePickerBottomSheetContent(
    selectedDate: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    val nowCalendar by rememberCurrentTimeState(1000L)

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
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.select_hour),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticalNumberWheel(
                items = (0..23).toList(),
                selectedItem = selectedHour,
                isItemEnabled = { hour -> !isToday || hour >= nowHour },
                onItemSelect = { hour ->
                    selectedHour = hour
                    if (isToday && hour == nowHour && selectedMinute <= nowMinute) {
                        selectedMinute = (nowMinute + 1) % 60
                    }
                },
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.hour)
            )

            Text(
                text = ":",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 4.dp).align(Alignment.CenterVertically)
            )

            VerticalNumberWheel(
                items = (0..59).toList(),
                selectedItem = selectedMinute,
                isItemEnabled = { minute ->
                    !(isToday && selectedHour == nowHour && minute <= nowMinute)
                },
                onItemSelect = { minute ->
                    selectedMinute = minute
                },
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.minutes)
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
        ) {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(
                onClick = { onConfirm(selectedHour, selectedMinute) },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun VerticalNumberWheel(
    items: List<Int>,
    selectedItem: Int,
    isItemEnabled: (Int) -> Boolean,
    onItemSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    label: String
) {
    val listState = rememberLazyListState()

    LaunchedEffect(selectedItem) {
        val index = items.indexOf(selectedItem)
        if (index >= 0) {
            listState.animateScrollToItem((index - 1).coerceAtLeast(0))
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 55.dp)
            ) {
                items(items) { item ->
                    val enabled = isItemEnabled(item)
                    val selected = item == selectedItem

                    val textColor by animateColorAsState(
                        targetValue = when {
                            !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f)
                            selected -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        },
                        label = ""
                    )

                    val textScale by animateDpAsState(
                        targetValue = if (selected) 24.dp else 16.dp,
                        label = ""
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .clickable(enabled = enabled) {
                                if (enabled) {
                                    onItemSelect(item)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format("%02d", item).toDigits(),
                            color = textColor,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = androidx.compose.ui.platform.LocalDensity.current.run { textScale.toSp() }
                            ),
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}