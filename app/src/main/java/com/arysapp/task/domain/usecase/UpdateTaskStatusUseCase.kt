package com.arysapp.task.domain.usecase

import com.arysapp.task.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskStatusUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: Long, isActive: Boolean): Result<Int> {
        return try {
            val result = taskRepository.updateTaskStatus(id, isActive)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}