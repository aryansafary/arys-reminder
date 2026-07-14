package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.repository.ReminderRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class RestoreRemindersUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(jsonContent: String): Result<Int> {
        return try {
            if (jsonContent.isBlank()) {
                return Result.failure(IllegalStateException("file is empty"))
            }

            val type = object : TypeToken<List<ReminderModel>>() {}.type
            val reminders: List<ReminderModel> = Gson().fromJson(jsonContent, type) ?: emptyList()

            if (reminders.isEmpty()) {
                return Result.failure(IllegalStateException("not found any reminder"))
            }

            reminderRepository.deleteAllReminders()

            reminders.forEach {
                reminderRepository.insertReminder(it)
            }

            Result.success(reminders.size)
        } catch (e: Exception) {
            Result.failure(Exception("invalid file", e))
        }
    }
}