package com.arysapp.reminder.ui.screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arysapp.reminder.R
import com.arysapp.reminder.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.reminder.utils.Constants.USER_LANGUAGE
import com.arysapp.reminder.ui.components.MyCalendar
import com.arysapp.reminder.ui.components.ReminderCard
import com.arysapp.reminder.ui.theme.toDigits
import com.arysapp.reminder.ui.viewmodel.CalendarViewModel
import com.arysapp.reminder.utils.Constants.ENGLISH_LANGUAGE
import com.arysapp.reminder.utils.StateResult
import com.arysapp.reminder.utils.helper.JalaliDate.Companion.getTodayDateString

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val isPersian = USER_LANGUAGE == PERSIAN_LANGUAGE
    var dateSelected by remember { mutableStateOf(getTodayDateString(isPersian)) }

    val remindersState by viewModel.remindersState.collectAsState()

    val allReminders = remember(remindersState) {
        if (remindersState is StateResult.Success) {
            (remindersState as StateResult.Success).data
        } else {
            emptyList()
        }
    }

    val datesWithReminders = remember(allReminders) {
        allReminders.map {
            it.dateTime.toString().
            toDigits(isPersian).
            toDigits(language = ENGLISH_LANGUAGE).
            trim()
        }.toSet()
    }

    val selectedDateReminders = remember(allReminders, dateSelected.trim()) {
        allReminders.filter {
            it.dateTime.
            toString().
            toDigits(isPersian).
            toDigits(language = ENGLISH_LANGUAGE).
            trim() == dateSelected
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {

        MyCalendar(
            selectedDate = dateSelected,
            isPersian = isPersian,
            datesWithReminders = datesWithReminders,
            onDateSelected = { dateSelected = it },
            modifier = Modifier.padding(16.dp)
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (remindersState) {
                is StateResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is StateResult.Error -> {
                    Text(
                        text = stringResource(R.string.error_get_data),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is StateResult.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(
                            items = selectedDateReminders,
                            key = { it.id }
                        ) { reminder ->
                            ReminderCard(
                                reminder = reminder,
                                onEdit = { },
                                onDelete = { viewModel.deleteReminder(it) },
                                onToggle = { isActive ->
                                    viewModel.updateReminderStatus(reminder.id, isActive)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}