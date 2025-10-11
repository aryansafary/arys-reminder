package com.arysapp.task.domain.usecase

import com.arysapp.task.domain.model.RepeatType
import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: TaskModel): Result<Int> {
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


            val result = taskRepository.updateTask(updatedTask)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}