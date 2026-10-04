package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.repository.ReminderRepository
import javax.inject.Inject

class InsertReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String? = null,
        dateTime: String? = null,
        hourTime: String? = null,
        repeatType: String = RepeatType.NONE.name,
        repeatIntervalDays: Int? = null,
        repeatIntervalWeeks: Int? = null,
        repeatIntervalMonths: Int? = null,
        repeatIntervalYears: Int? = null,
        reminderMinutesBefore: Int? = null,
        category: String
    ): Result<Long> {
        return try {
            if (title.isBlank()) {
                return Result.failure(IllegalArgumentException("عنوان تسک نمی‌تواند خالی باشد"))
            }
            val validRepeatTypes = RepeatType.entries.map { it.name }
            val validatedRepeatType = if (repeatType in validRepeatTypes) repeatType else RepeatType.NONE.name
            if (validatedRepeatType != RepeatType.NONE.name &&
                repeatIntervalDays == null &&
                repeatIntervalWeeks == null &&
                repeatIntervalMonths == null &&
                repeatIntervalYears == null) {
                return Result.failure(IllegalArgumentException("برای تسک تکراری باید حداقل یک بازه زمانی مشخص شود"))
            }
           // val currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val task = ReminderModel(
                title = title.trim(),
                description = description?.trim(),
                dateTime = dateTime,
                hourTime = hourTime,
                repeatType = validatedRepeatType,
                repeatIntervalDays = repeatIntervalDays,
                repeatIntervalWeeks = repeatIntervalWeeks,
                repeatIntervalMonths = repeatIntervalMonths,
                repeatIntervalYears = repeatIntervalYears,
                isActive = true,
                reminderMinutesBefore = reminderMinutesBefore,
                createdAt = "currentTime",
                updatedAt = "currentTime",
                category = category
            )
            val taskId = reminderRepository.insertReminder(task)
            Result.success(taskId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}