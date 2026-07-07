package com.arysapp.reminder.domain.usecase


import com.arysapp.reminder.domain.repository.ReminderRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BackupReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {

    suspend operator fun invoke(): Result<String> {
        return try {
            val tasks = reminderRepository.getAllTasks().first()

            if (tasks.isEmpty()) {
                return Result.failure(IllegalStateException("No reminders"))
            }

            Result.success(Gson().toJson(tasks))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}