package com.arysapp.task.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arysapp.task.domain.model.RepeatType
import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.core.alarm.TaskReminderScheduler
import com.arysapp.task.domain.usecase.TaskUseCases
import com.arysapp.task.utils.StateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases,
    private val reminderScheduler: TaskReminderScheduler
) : ViewModel() {

    private val _tasks = MutableStateFlow<StateResult<List<TaskModel>>>(StateResult.Loading)
    val tasks: StateFlow<StateResult<List<TaskModel>>> = _tasks.asStateFlow()

    init {
        refreshTasksWithProcessing()
    }

    private fun refreshTasksWithProcessing() {
        viewModelScope.launch {
            _tasks.value = StateResult.Loading
            try {
                taskUseCases.processExpiredTasks()
                taskUseCases.getAllTasks().collect { tasksList ->
                    _tasks.value = StateResult.Success(tasksList)
                    reminderScheduler.scheduleRemindersForTasks(tasksList)
                }
            } catch (e: Exception) {
                _tasks.value = StateResult.Error(e.message ?: "Error loading tasks")
            }
        }
    }

    fun getAllTasks() {
        refreshTasksWithProcessing()
    }

    fun insertTask(
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
            _tasks.value = StateResult.Loading
            val result = taskUseCases.insertTask(
                title, description, dateTime, hourTime,
                repeatType, repeatIntervalDays, repeatIntervalWeeks,
                repeatIntervalMonths, reminderMinutesBefore
            )
            if (result.isSuccess) {
                refreshTasksWithProcessing()
                reminderScheduler.scheduleAllReminders()
            } else if (result.isFailure) {
                _tasks.value = StateResult.Error(
                    result.exceptionOrNull()?.message ?: "Error inserting task"
                )
            }
        }
    }

    fun updateTask(task: TaskModel) {
        viewModelScope.launch {
            _tasks.value = StateResult.Loading
            val result = taskUseCases.updateTask(task)
            if (result.isSuccess) {
                refreshTasksWithProcessing()
                reminderScheduler.scheduleAllReminders()
            } else if (result.isFailure) {
                _tasks.value = StateResult.Error(
                    result.exceptionOrNull()?.message ?: "Error updating task"
                )
            }
        }
    }

    fun deleteTask(task: TaskModel) {
        viewModelScope.launch {
            _tasks.value = StateResult.Loading
            val result = taskUseCases.deleteTask(task)
            if (result.isSuccess) {
                refreshTasksWithProcessing()
                reminderScheduler.cancelReminderForTask(task.id)
            } else if (result.isFailure) {
                _tasks.value = StateResult.Error(
                    result.exceptionOrNull()?.message ?: "Error deleting task"
                )
            }
        }
    }

    fun updateTaskStatus(taskId: Long, isActive: Boolean) {
        viewModelScope.launch {
            _tasks.value = StateResult.Loading
            val result = taskUseCases.updateTaskStatus(taskId, isActive)
            if (result.isSuccess) {
                refreshTasksWithProcessing()
            } else if (result.isFailure) {
                _tasks.value = StateResult.Error(
                    result.exceptionOrNull()?.message ?: "Error updating task status"
                )
            }
        }
    }
}
