package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.repository.ReminderRepository
import javax.inject.Inject

class DeleteReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(task: ReminderModel): Result<Int> {
        return try {
            val result = reminderRepository.deleteReminder(task)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}