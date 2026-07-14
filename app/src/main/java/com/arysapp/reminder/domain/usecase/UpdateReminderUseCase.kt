package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.repository.ReminderRepository
import javax.inject.Inject

class UpdateReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(reminder: ReminderModel): Result<Int> {
        return try {
            if (reminder.title.isBlank()) {
                return Result.failure(IllegalArgumentException("عنوان تسک نمی‌تواند خالی باشد"))
            }

            val validRepeatTypes = RepeatType.entries.map { it.name }
            val validatedRepeatType = if (reminder.repeatType in validRepeatTypes) {
                reminder.repeatType
            } else {
                return Result.failure(IllegalArgumentException("نوع تکرار نامعتبر است"))
            }


            if (validatedRepeatType != RepeatType.NONE.name &&
                reminder.repeatIntervalDays == null &&
                reminder.repeatIntervalWeeks == null &&
                reminder.repeatIntervalMonths == null &&
                reminder.repeatIntervalYears == null
            ) {
                return Result.failure(IllegalArgumentException("برای تسک تکراری باید حداقل یک بازه زمانی مشخص شود"))
            }

            val updatedTask = reminder.copy(
                updatedAt = ""//LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )


            val result = reminderRepository.updateReminder(updatedTask)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}