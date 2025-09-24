package com.arysapp.task.domain.usecase

import com.arysapp.task.domain.model.RepeatType
import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.domain.repository.TaskRepository
import javax.inject.Inject

class InsertTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
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
        reminderMinutesBefore: Int? = null
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
                repeatIntervalMonths == null) {
                return Result.failure(IllegalArgumentException("برای تسک تکراری باید حداقل یک بازه زمانی مشخص شود"))
            }
           // val currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val task = TaskModel(
                title = title.trim(),
                description = description?.trim(),
                dateTime = dateTime,
                hourTime = hourTime,
                repeatType = validatedRepeatType,
                repeatIntervalDays = repeatIntervalDays,
                repeatIntervalWeeks = repeatIntervalWeeks,
                repeatIntervalMonths = repeatIntervalMonths,
                isActive = true,
                reminderMinutesBefore = reminderMinutesBefore,
                createdAt = "currentTime",
                updatedAt = "currentTime"
            )
            val taskId = taskRepository.insertTask(task)
            Result.success(taskId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}