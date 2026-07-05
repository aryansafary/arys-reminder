package com.arysapp.reminder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.core.alarm.ReminderScheduler
import com.arysapp.reminder.domain.usecase.ReminderUseCases
import com.arysapp.reminder.utils.StateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderUseCases: ReminderUseCases,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    private val _reminders = MutableStateFlow<StateResult<List<ReminderModel>>>(StateResult.Loading)
    val reminders: StateFlow<StateResult<List<ReminderModel>>> = _reminders.asStateFlow()

    init {
        refreshRemindersWithProcessing()
    }

    private fun refreshRemindersWithProcessing() {
        viewModelScope.launch {
            _reminders.value = StateResult.Loading
            try {
                reminderUseCases.processExpiredReminders()
                reminderUseCases.getAllReminders().collect { tasksList ->
                    _reminders.value = StateResult.Success(tasksList)
                    reminderScheduler.scheduleRemindersForReminders(tasksList)
                }
            } catch (e: Exception) {
                _reminders.value = StateResult.Error(e.message ?: "Error loading tasks")
            }
        }
    }

    fun getAllReminders() {
        refreshRemindersWithProcessing()
    }

    fun insertReminder(
        title: String,
        description: String? = null,
        dateTime: String? = null,
        hourTime: String? = null,
        repeatType: String = RepeatType.NONE.name,
        repeatIntervalDays: Int? = null,
        repeatIntervalWeeks: Int? = null,
        repeatIntervalMonths: Int? = null,
        reminderMinutesBefore: Int? = null
    ) {
        viewModelScope.launch {
            _reminders.value = StateResult.Loading
            val result = reminderUseCases.insertReminder(
                title, description, dateTime, hourTime,
                repeatType, repeatIntervalDays, repeatIntervalWeeks,
                repeatIntervalMonths, reminderMinutesBefore
            )
            if (result.isSuccess) {
                refreshRemindersWithProcessing()
                reminderScheduler.scheduleAllReminders()
            } else if (result.isFailure) {
                _reminders.value = StateResult.Error(
                    result.exceptionOrNull()?.message ?: "Error inserting task"
                )
            }
        }
    }

    fun updateReminder(reminder: ReminderModel) {
        viewModelScope.launch {
            _reminders.value = StateResult.Loading
            val result = reminderUseCases.updateReminder(reminder)
            if (result.isSuccess) {
                refreshRemindersWithProcessing()
                reminderScheduler.scheduleAllReminders()
            } else if (result.isFailure) {
                _reminders.value = StateResult.Error(
                    result.exceptionOrNull()?.message ?: "Error updating task"
                )
            }
        }
    }

    fun deleteReminder(reminder: ReminderModel) {
        viewModelScope.launch {
            _reminders.value = StateResult.Loading
            val result = reminderUseCases.deleteReminder(reminder)
            if (result.isSuccess) {
                refreshRemindersWithProcessing()
                reminderScheduler.cancelReminderForReminder(reminder.id)
            } else if (result.isFailure) {
                _reminders.value = StateResult.Error(
                    result.exceptionOrNull()?.message ?: "Error deleting task"
                )
            }
        }
    }

    fun updateReminderStatus(reminderId: Long, isActive: Boolean) {
        viewModelScope.launch {
            _reminders.value = StateResult.Loading
            val result = reminderUseCases.updateReminderStatus(reminderId, isActive)
            if (result.isSuccess) {
                refreshRemindersWithProcessing()
            } else if (result.isFailure) {
                _reminders.value = StateResult.Error(
                    result.exceptionOrNull()?.message ?: "Error updating task status"
                )
            }
        }
    }
}
