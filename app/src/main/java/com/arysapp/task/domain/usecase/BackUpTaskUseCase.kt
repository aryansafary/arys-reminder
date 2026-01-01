package com.arysapp.task.domain.usecase


import com.arysapp.task.domain.repository.TaskRepository
import com.arysapp.task.utils.helper.JsonUtils.toJsonFile
import kotlinx.coroutines.flow.first
import java.io.File
import javax.inject.Inject

class BackupTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(backupFile: File): Result<File> {
        return try {
            val tasks = taskRepository.getAllTasks().first()
            if (tasks.isEmpty()) {
                return Result.failure(IllegalStateException())
            }
            val savedFile = tasks.toJsonFile(backupFile)
            Result.success(savedFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}