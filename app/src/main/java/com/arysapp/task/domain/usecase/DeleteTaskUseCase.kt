package com.arysapp.task.domain.usecase

import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: TaskModel): Result<Int> {
        return try {
            val result = taskRepository.deleteTask(task)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}