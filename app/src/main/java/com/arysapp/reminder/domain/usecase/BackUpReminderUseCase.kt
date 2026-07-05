package com.arysapp.reminder.domain.usecase


import com.arysapp.reminder.domain.repository.ReminderRepository
import com.arysapp.reminder.utils.helper.JsonUtils.toJsonFile
import kotlinx.coroutines.flow.first
import java.io.File
import javax.inject.Inject

class BackupReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(backupFile: File): Result<File> {
        return try {
            val tasks = reminderRepository.getAllTasks().first()
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