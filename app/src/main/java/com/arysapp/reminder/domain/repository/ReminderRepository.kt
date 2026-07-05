package com.arysapp.reminder.domain.repository

import com.arysapp.reminder.domain.model.ReminderModel
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getAllTasks(): Flow<List<ReminderModel>>
    fun getActiveTasks(): Flow<List<ReminderModel>>
    suspend fun getTaskById(id: Long): ReminderModel?
    suspend fun insertTask(task: ReminderModel): Long
    suspend fun insertTasks(tasks: List<ReminderModel>): List<Long>
    suspend fun updateTask(task: ReminderModel): Int
    suspend fun deleteTask(task: ReminderModel): Int
    suspend fun deleteAllTasks(): Int
    suspend fun deleteTaskById(id: Long): Int
    suspend fun updateTaskStatus(id: Long, isActive: Boolean): Int
    suspend fun updateTaskDateTime(id: Long, newDateTime: String, newHourTime: String?): Int
    suspend fun getAllTasksOnce(): List<ReminderModel>
}