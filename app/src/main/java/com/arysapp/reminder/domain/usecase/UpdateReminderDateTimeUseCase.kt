package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.repository.ReminderRepository
import javax.inject.Inject

class UpdateReminderDateTimeUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(id: Long, newDateTime: String, newHourTime: String?): Result<Int> {
        return try {
            val result = reminderRepository.updateReminderDateTime(id, newDateTime, newHourTime)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
