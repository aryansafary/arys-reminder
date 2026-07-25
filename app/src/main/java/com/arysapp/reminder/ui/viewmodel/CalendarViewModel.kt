package com.arysapp.reminder.ui.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.usecase.ReminderUseCases
import com.arysapp.reminder.utils.StateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val reminderUseCases: ReminderUseCases
) : ViewModel() {

    private val _remindersState = MutableStateFlow<StateResult<List<ReminderModel>>>(StateResult.Loading)
    val remindersState: StateFlow<StateResult<List<ReminderModel>>> = _remindersState.asStateFlow()

    init {
        getAllReminders()
    }

    private fun getAllReminders() {
        viewModelScope.launch {
            _remindersState.value = StateResult.Loading
            try {
                reminderUseCases.getAllReminders().collect { tasksList ->
                    _remindersState.value = StateResult.Success(tasksList)
                }
            } catch (e: Exception) {
                _remindersState.value = StateResult.Error(e.message ?: "Error loading reminders for calendar")
            }
        }
    }

    fun updateReminderStatus(reminderId: Long, isActive: Boolean) {
        viewModelScope.launch {
            reminderUseCases.updateReminderStatus(reminderId, isActive)
        }
    }

    fun deleteReminder(reminder: ReminderModel) {
        viewModelScope.launch {
            reminderUseCases.deleteReminder(reminder)
        }
    }
}