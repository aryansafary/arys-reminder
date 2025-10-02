package com.arysapp.task.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arysapp.task.utils.helper.JalaliDate
import java.util.Calendar
import com.arysapp.task.R
import com.arysapp.task.ui.theme.toEnglishDigits
import com.arysapp.task.ui.theme.toPersianDigits
import com.arysapp.task.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.task.utils.Constants.USER_LANGUAGE
import java.util.GregorianCalendar

@Composable
fun MyDatePicker(
    selectedDate: String,
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        OutlinedButton(
            onClick = { showPicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if(USER_LANGUAGE==PERSIAN_LANGUAGE) {selectedDate.toPersianDigits()} else {selectedDate.toEnglishDigits()}.ifEmpty { stringResource(R.string.selecting_date) },
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (showPicker) {
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

    PickerCard(
        title = stringResource(R.string.selecting_date),
        onDismiss = onDismiss,
        onConfirm = {
            if (isValidDate(year, month, day)) {
                onConfirm(
                    "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
                )
            }
        }
    ) {
        Row {
                NumberPickerComposable(
                    value = year,
                    range = today.get(Calendar.YEAR)..2100,
                    onValueChange = { year = it }
                )
                Spacer(Modifier.width(8.dp))

                NumberPickerComposable(
                    value = month,
                    range = if (year == today.get(Calendar.YEAR)) (today.get(Calendar.MONTH) + 1)..12 else 1..12,
                    onValueChange = {
                        month = it
                        val maxDay = daysInMonth(year, month)
                        if (day > maxDay) day = maxDay
                    }
                )
                Spacer(Modifier.width(8.dp))

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
    PickerCard(
    title = stringResource(R.string.selecting_date),
    onDismiss = onDismiss,
    onConfirm = {
        val selected = "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
        if (isValidDate(JalaliDate(year, month, day))) {
            onConfirm(selected)
        }
    },
        isValidDate(JalaliDate(year, month, day))
    ) {
            Row {

                NumberPickerComposable(
                    value = day,
                    range = if (year == today.year && month == today.month)
                        today.day..daysInMonth
                    else 1..daysInMonth,
                    onValueChange = { day = it }
                )
                Spacer(Modifier.width(8.dp))
                NumberPickerComposable(
                    value = month,
                    range = if (year == today.year) today.month..12 else 1..12,
                    onValueChange = { month = it }
                )
                Spacer(Modifier.width(8.dp))
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
    var currentValue by remember { mutableIntStateOf(value) }

    val textColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.primary,
        label = ""
    )

    val animatedSize by animateDpAsState(
        targetValue = if (currentValue == value) 24.dp else 20.dp,
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
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                )
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = "Increase",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = if(USER_LANGUAGE==PERSIAN_LANGUAGE) currentValue.toString().toPersianDigits() else currentValue.toString().toEnglishDigits(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = with(LocalDensity.current) { animatedSize.toSp() }
            ),
            color = textColor,
            modifier = Modifier.padding(8.dp)
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
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                )
        ) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Decrease",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
@Composable
fun PickerCard(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isValid: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            content()

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = onConfirm,
                    shape = RoundedCornerShape(12.dp),
                    enabled = isValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = stringResource(R.string.confirm))
                }
                }
            }
        }
    }










