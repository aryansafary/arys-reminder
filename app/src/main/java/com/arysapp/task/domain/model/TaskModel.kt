package com.arysapp.task.domain.model



data class TaskModel(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val dateTime: String? = null,
    val hourTime: String? = null,
    val repeatType: String = RepeatType.NONE.name,
    val repeatIntervalDays: Int? = null,
    val repeatIntervalWeeks: Int? = null,
    val repeatIntervalMonths: Int? = null,
    val isActive: Boolean = true,
    val reminderMinutesBefore: Int? = null,
    val createdAt: String,
    val updatedAt: String
)
