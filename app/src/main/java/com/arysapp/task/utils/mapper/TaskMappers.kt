package com.arysapp.task.utils.mapper

import com.arysapp.task.data.local.entity.TaskEntity
import com.arysapp.task.domain.model.TaskModel


fun TaskEntity.toDomain(): TaskModel  {
    return TaskModel(
        id = id,
        title = title,
        description = description,
        dateTime = dateTime,
        hourTime = hourTime,
        repeatType = repeatType,
        repeatIntervalDays = repeatIntervalDays,
        repeatIntervalWeeks = repeatIntervalWeeks,
        repeatIntervalMonths = repeatIntervalMonths,
        isActive = isActive,
        reminderMinutesBefore = reminderMinutesBefore,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun TaskModel.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        dateTime = dateTime,
        hourTime = hourTime,
        repeatType = repeatType,
        repeatIntervalDays = repeatIntervalDays,
        repeatIntervalWeeks = repeatIntervalWeeks,
        repeatIntervalMonths = repeatIntervalMonths,
        isActive = isActive,
        reminderMinutesBefore = reminderMinutesBefore,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
