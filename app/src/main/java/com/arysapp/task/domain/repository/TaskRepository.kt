package com.arysapp.task.domain.repository

import com.arysapp.task.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<TaskModel>>
    fun getActiveTasks(): Flow<List<TaskModel>>
    suspend fun getTaskById(id: Long): TaskModel?
    suspend fun insertTask(task: TaskModel): Long
    suspend fun insertTasks(tasks: List<TaskModel>): List<Long>
    suspend fun updateTask(task: TaskModel): Int
    suspend fun deleteTask(task: TaskModel): Int
    suspend fun deleteTaskById(id: Long): Int
    suspend fun updateTaskStatus(id: Long, isActive: Boolean): Int
    suspend fun updateTaskDateTime(id: Long, newDateTime: String, newHourTime: String?): Int
}