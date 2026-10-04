package com.arysapp.reminder.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arysapp.reminder.utils.helper.JalaliDate
import java.util.Calendar
import com.arysapp.reminder.R
import com.arysapp.reminder.ui.theme.toDigits
import com.arysapp.reminder.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.reminder.utils.Constants.USER_LANGUAGE
import java.util.GregorianCalendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    selectedDate: String,
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            //.padding(4.dp)
    ) {
        OutlinedButton(
            onClick = { showPicker = true },
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp , bottom = 8.dp)

            ) {
                Icon(
                    painter = painterResource(R.drawable.calendar_icon),
                    contentDescription = "calendar",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier= Modifier.size(24.dp)
                )
                Text(
                    text = selectedDate.toDigits()
                        .ifEmpty { stringResource(R.string.selecting_date) },
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
                Box(modifier = Modifier.padding(bottom = 36.dp, start = 20.dp, end = 20.dp)) {
                    if (USER_LANGUAGE == PERSIAN_LANGUAGE) {
                        JalaliDatePicker(
                            onDismiss = { showPicker = false },
                            onConfirm = {
                                onDateSelected(it)
                                showPicker = false
                            }
                        )
                    } else {
                        GregorianDatePicker(
                            onDismiss = { showPicker = false },
                            onConfirm = {
                                onDateSelected(it)
                                showPicker = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GregorianDatePicker(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val today = Calendar.getInstance()
    var year by remember { mutableIntStateOf(today.get(Calendar.YEAR)) }
    var month by remember { mutableIntStateOf(today.get(Calendar.MONTH) + 1) }
    var day by remember { mutableIntStateOf(today.get(Calendar.DAY_OF_MONTH)) }

    fun daysInMonth(y: Int, m: Int): Int {
        return when (m) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (GregorianCalendar().isLeapYear(y)) 29 else 28
            else -> 30
        }
    }

    fun isValidDate(y: Int, m: Int, d: Int): Boolean {
        val cal = Calendar.getInstance().apply { set(y, m - 1, d, 0, 0, 0) }
        return !cal.before(today)
    }

    PickerBottomSheetContent(
        title = stringResource(R.string.selecting_date),
        onDismiss = onDismiss,
        onConfirm = {
            if (isValidDate(year, month, day)) {
                onConfirm(
                    "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
                )
            }
        },
        isValid = isValidDate(year, month, day)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumberPickerComposable(
                value = year,
                range = today.get(Calendar.YEAR)..2100,
                onValueChange = { year = it }
            )
            Spacer(Modifier.width(16.dp))

            NumberPickerComposable(
                value = month,
                range = if (year == today.get(Calendar.YEAR)) (today.get(Calendar.MONTH) + 1)..12 else 1..12,
                onValueChange = {
                    month = it
                    val maxDay = daysInMonth(year, month)
                    if (day > maxDay) day = maxDay
                }
            )
            Spacer(Modifier.width(16.dp))

            NumberPickerComposable(
                value = day,
                range = if (year == today.get(Calendar.YEAR) && month == today.get(Calendar.MONTH) + 1)
                    today.get(Calendar.DAY_OF_MONTH)..daysInMonth(year, month)
                else 1..daysInMonth(year, month),
                onValueChange = { day = it }
            )
        }
    }
}

@Composable
fun JalaliDatePicker(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val today = JalaliDate.today()
    var year by remember { mutableIntStateOf(today.year) }
    var month by remember { mutableIntStateOf(today.month) }
    var day by remember { mutableIntStateOf(today.day) }

    val daysInMonth = JalaliDate().getDaysInJalaliMonth(year, month)

    fun isValidDate(date: JalaliDate): Boolean {
        return date >= today
    }

    PickerBottomSheetContent(
        title = stringResource(R.string.selecting_date),
        onDismiss = onDismiss,
        onConfirm = {
            val selected = "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
            if (isValidDate(JalaliDate(year, month, day))) {
                onConfirm(selected)
            }
        },
        isValid = isValidDate(JalaliDate(year, month, day))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumberPickerComposable(
                value = day,
                range = if (year == today.year && month == today.month)
                    today.day..daysInMonth
                else 1..daysInMonth,
                onValueChange = { day = it }
            )
            Spacer(Modifier.width(16.dp))
            NumberPickerComposable(
                value = month,
                range = if (year == today.year) today.month..12 else 1..12,
                onValueChange = { month = it }
            )
            Spacer(Modifier.width(16.dp))
            NumberPickerComposable(
                value = year,
                range = today.year..1500,
                onValueChange = { year = it }
            )
        }
    }
}

@Composable
fun NumberPickerComposable(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    var currentValue by remember(value) { mutableIntStateOf(value) }

    val textColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.primary,
        label = ""
    )

    val animatedSize by animateDpAsState(
        targetValue = if (currentValue == value) 22.dp else 18.dp,
        label = ""
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = {
                if (currentValue < range.last) {
                    currentValue++
                    onValueChange(currentValue)
                }
            },
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = CircleShape
                )
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = "Increase",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = currentValue.toString().toDigits(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = with(LocalDensity.current) { animatedSize.toSp() }
            ),
            color = textColor,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        IconButton(
            onClick = {
                if (currentValue > range.first) {
                    currentValue--
                    onValueChange(currentValue)
                }
            },
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = CircleShape
                )
        ) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Decrease",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PickerBottomSheetContent(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isValid: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        content()

        Spacer(Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
        ) {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(14.dp),
                enabled = isValid,
                modifier = Modifier.weight(1f).height(50.dp),
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