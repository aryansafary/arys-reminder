package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.repository.ReminderRepository
import com.arysapp.reminder.utils.helper.JsonUtils.fromJsonToReminders
import java.io.File
import javax.inject.Inject

class RestoreRemindersUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(restoreFile: File): Result<Int> {
        return try {
            val tasks = restoreFile.fromJsonToReminders()
            if (tasks.isEmpty()) {
                return Result.failure(IllegalStateException())
            }
            // پاک کردن قدیمی‌ها (clean restore)
            reminderRepository.deleteAllTasks()
            // Insert جدید
            val insertedCount = tasks.map { reminderRepository.insertTask(it) }.size
            Result.success(insertedCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}