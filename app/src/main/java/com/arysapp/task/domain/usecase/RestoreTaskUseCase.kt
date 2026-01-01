package com.arysapp.task.domain.usecase

import com.arysapp.task.domain.repository.TaskRepository
import com.arysapp.task.utils.helper.JsonUtils.fromJsonToTasks
import java.io.File
import javax.inject.Inject

class RestoreTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(restoreFile: File): Result<Int> {
        return try {
            val tasks = restoreFile.fromJsonToTasks()
            if (tasks.isEmpty()) {
                return Result.failure(IllegalStateException())
            }
            // پاک کردن قدیمی‌ها (clean restore)
            taskRepository.deleteAllTasks()
            // Insert جدید
            val insertedCount = tasks.map { taskRepository.insertTask(it) }.size
            Result.success(insertedCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}