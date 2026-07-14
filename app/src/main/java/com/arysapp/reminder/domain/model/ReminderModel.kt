package com.arysapp.reminder.domain.model



data class ReminderModel(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val dateTime: String? = null,
    val hourTime: String? = null,
    val repeatType: String = RepeatType.NONE.name,
    val repeatIntervalDays: Int? = null,
    val repeatIntervalWeeks: Int? = null,
    val repeatIntervalMonths: Int? = null,
    val repeatIntervalYears: Int? = null,
    val isActive: Boolean = true,
    val reminderMinutesBefore: Int? = null,
    val createdAt: String,
    val updatedAt: String
)
