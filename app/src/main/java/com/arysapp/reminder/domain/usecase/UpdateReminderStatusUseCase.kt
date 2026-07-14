package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.repository.ReminderRepository
import javax.inject.Inject

class UpdateReminderStatusUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(id: Long, isActive: Boolean): Result<Int> {
        return try {
            val result = reminderRepository.updateReminderStatus(id, isActive)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}