package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.repository.ReminderRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import javax.inject.Inject

class RestoreRemindersUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {

    suspend operator fun invoke(inputStream: InputStream): Result<Int> {
        return try {

            val json = inputStream.bufferedReader().use { it.readText() }

            val type = object : TypeToken<List<ReminderModel>>() {}.type
            val reminders: List<ReminderModel> =
                Gson().fromJson(json, type) ?: emptyList()

            if (reminders.isEmpty()) {
                return Result.failure(IllegalStateException("Backup file is empty"))
            }

            reminderRepository.deleteAllTasks()

            reminders.forEach {
                reminderRepository.insertTask(it)
            }

            Result.success(reminders.size)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}