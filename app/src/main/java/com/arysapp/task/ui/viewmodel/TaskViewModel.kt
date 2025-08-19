package com.arysapp.task.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arysapp.task.domain.model.RepeatType
import com.arysapp.task.domain.model.TaskModel
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
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    private val _tasks = MutableStateFlow<StateResult<List<TaskModel>>>(StateResult.Loading)
    val tasks: StateFlow<StateResult<List<TaskModel>>> = _tasks.asStateFlow()

    private val _selectedTask = MutableStateFlow<TaskModel?>(null)
    val selectedTask: StateFlow<TaskModel?> = _selectedTask.asStateFlow()

    init {
        getAllTasks()
    }

    fun getAllTasks() {
        viewModelScope.launch {
            _tasks.value = StateResult.Loading
            try {
                taskUseCases.getAllTasks().collect { tasksList ->
                    _tasks.value = StateResult.Success(tasksList)
                }
            } catch (e: Exception) {
                _tasks.value = StateResult.Error(e.message ?: "خطا در بارگذاری تسک‌ها")
            }
        }
    }

    fun getActiveTasks() {
        viewModelScope.launch {
            _tasks.value = StateResult.Loading
            try {
                taskUseCases.getAllTasks().collect { tasksList ->
                    _tasks.value = StateResult.Success(tasksList.filter { it.isActive })
                }
            } catch (e: Exception) {
                _tasks.value = StateResult.Error(e.message ?: "خطا در بارگذاری تسک‌های فعال")
            }
        }
    }

    fun insertTask(
        title: String,
        description: String? = null,
        startTime: String? = null,
        endTime: String? = null,
        repeatType: String = RepeatType.NONE.name,
        repeatIntervalDays: Int? = null,
        repeatIntervalWeeks: Int? = null,
        repeatIntervalMonths: Int? = null,
        reminderMinutesBefore: Int? = null
    ) {
        viewModelScope.launch {
            _tasks.value = StateResult.Loading
            val result = taskUseCases.insertTask(
                title, description, startTime, endTime,
                repeatType, repeatIntervalDays, repeatIntervalWeeks,
                repeatIntervalMonths, reminderMinutesBefore
            )
            // استفاده از isSuccess و isFailure به جای Success و Failure
            if (result.isSuccess) {
                getAllTasks()
            } else if (result.isFailure) {
                _tasks.value = StateResult.Error(
                    result.exceptionOrNull()?.message ?: "خطا در افزودن تسک"
                )
            }
        }
    }

    fun updateTask(task: TaskModel) {
        viewModelScope.launch {
            _tasks.value = StateResult.Loading
            val result = taskUseCases.updateTask(task)
            if (result.isSuccess) {
                getAllTasks()
            } else if (result.isFailure) {
                _tasks.value = StateResult.Error(
                    result.exceptionOrNull()?.message ?: "خطا در به‌روزرسانی تسک"
                )
            }
        }
    }

    fun deleteTask(task: TaskModel) {
        viewModelScope.launch {
            _tasks.value = StateResult.Loading
            val result = taskUseCases.deleteTask(task)
            if (result.isSuccess) {
                getAllTasks()
            } else if (result.isFailure) {
                _tasks.value = StateResult.Error(
                    result.exceptionOrNull()?.message ?: "خطا در حذف تسک"
                )
            }
        }
    }

    fun updateTaskStatus(taskId: Long, isActive: Boolean) {
        viewModelScope.launch {
            _tasks.value = StateResult.Loading
            val result = taskUseCases.updateTaskStatus(taskId, isActive)
            if (result.isSuccess) {
                getAllTasks()
            } else if (result.isFailure) {
                _tasks.value = StateResult.Error(
                    result.exceptionOrNull()?.message ?: "خطا در به‌روزرسانی وضعیت"
                )
            }
        }
    }



    fun selectTask(task: TaskModel) {
        _selectedTask.value = task
    }
}