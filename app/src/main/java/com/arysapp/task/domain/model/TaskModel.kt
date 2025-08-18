package com.arysapp.task.domain.model



data class TaskModel(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val repeatType: String = RepeatType.NONE.name,
    val repeatIntervalDays: Int? = null,
    val repeatIntervalWeeks: Int? = null,
    val repeatIntervalMonths: Int? = null,
    val isActive: Boolean = true,
    val reminderMinutesBefore: Int? = null,
    val createdAt: String,
    val updatedAt: String
)
