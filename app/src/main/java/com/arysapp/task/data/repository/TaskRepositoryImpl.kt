package com.arysapp.task.data.repository

import com.arysapp.task.data.local.dao.TaskDao
import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.domain.repository.TaskRepository
import com.arysapp.task.utils.extension.toDomain
import com.arysapp.task.utils.extension.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<TaskModel>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getActiveTasks(): Flow<List<TaskModel>> {
        return taskDao.getActiveTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(id: Long): TaskModel? {
        return taskDao.getTaskById(id)?.toDomain()
    }

    override suspend fun insertTask(task: TaskModel): Long {
        return taskDao.insertTask(task.toEntity())
    }

    override suspend fun insertTasks(tasks: List<TaskModel>): List<Long> {
        return taskDao.insertTasks(tasks.map { it.toEntity() })
    }

    override suspend fun updateTask(task: TaskModel): Int {
        return taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: TaskModel): Int {
        return taskDao.deleteTask(task.toEntity())
    }

    override suspend fun deleteTaskById(id: Long): Int {
        return taskDao.deleteTaskById(id)
    }

    override suspend fun updateTaskStatus(id: Long, isActive: Boolean): Int {
        return taskDao.updateTaskStatus(id, isActive)
    }
}