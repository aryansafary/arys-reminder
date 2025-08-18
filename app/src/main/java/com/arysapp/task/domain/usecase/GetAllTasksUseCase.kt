package com.arysapp.task.domain.usecase

import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<TaskModel>> {
        return taskRepository.getAllTasks()
    }
}