package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.repository.ReminderRepository
import javax.inject.Inject

class UpdateReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(task: ReminderModel): Result<Int> {
        return try {
            if (task.title.isBlank()) {
                return Result.failure(IllegalArgumentException("عنوان تسک نمی‌تواند خالی باشد"))
            }

            // اعتبارسنجی repeatType
            val validRepeatTypes = RepeatType.entries.map { it.name }
            val validatedRepeatType = if (task.repeatType in validRepeatTypes) {
                task.repeatType
            } else {
                return Result.failure(IllegalArgumentException("نوع تکرار نامعتبر است"))
            }


            if (validatedRepeatType != RepeatType.NONE.name &&
                task.repeatIntervalDays == null &&
                task.repeatIntervalWeeks == null &&
                task.repeatIntervalMonths == null
            ) {
                return Result.failure(IllegalArgumentException("برای تسک تکراری باید حداقل یک بازه زمانی مشخص شود"))
            }

            val updatedTask = task.copy(
                updatedAt = ""//LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )


            val result = reminderRepository.updateTask(updatedTask)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}