package com.arysapp.task.domain.usecase

import com.arysapp.task.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskDateTimeUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: Long, newDateTime: String, newHourTime: String?): Result<Int> {
        return try {
            val result = taskRepository.updateTaskDateTime(id, newDateTime, newHourTime)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
