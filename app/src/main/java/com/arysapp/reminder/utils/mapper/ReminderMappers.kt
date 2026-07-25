package com.arysapp.reminder.utils.mapper

import com.arysapp.reminder.data.local.entity.ReminderEntity
import com.arysapp.reminder.domain.model.ReminderModel


fun ReminderEntity.toDomain(): ReminderModel  {
    return ReminderModel(
        id = id,
        title = title,
        description = description,
        dateTime = dateTime?.trim(),
        hourTime = hourTime?.trim(),
        repeatType = repeatType,
        repeatIntervalDays = repeatIntervalDays,
        repeatIntervalWeeks = repeatIntervalWeeks,
        repeatIntervalMonths = repeatIntervalMonths,
        repeatIntervalYears = repeatIntervalYears,
        isActive = isActive,
        reminderMinutesBefore = reminderMinutesBefore,
        createdAt = createdAt.trim(),
        updatedAt = updatedAt.trim()
    )
}

fun ReminderModel.toEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        title = title,
        description = description,
        dateTime = dateTime?.trim(),
        hourTime = hourTime?.trim(),
        repeatType = repeatType,
        repeatIntervalDays = repeatIntervalDays,
        repeatIntervalWeeks = repeatIntervalWeeks,
        repeatIntervalMonths = repeatIntervalMonths,
        repeatIntervalYears = repeatIntervalYears,
        isActive = isActive,
        reminderMinutesBefore = reminderMinutesBefore,
        createdAt = createdAt.trim(),
        updatedAt = updatedAt.trim()
    )
}
