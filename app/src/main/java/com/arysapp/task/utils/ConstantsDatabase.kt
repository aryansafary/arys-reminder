package com.arysapp.task.utils

object ConstantsDatabase {
    const val TASK_TABLE_NAME = "tasks"
    const val DATABASE_NAME = "ArysTaskDatabase"

    const val COLUMN_TASK_ID = "task_id"
    const val COLUMN_TASK_TITLE = "task_title"
    const val COLUMN_TASK_DESCRIPTION = "task_description"
    const val COLUMN_TASK_START_TIME = "task_start_time"
    const val COLUMN_TASK_END_TIME = "task_end_time"
    const val COLUMN_TASK_REPEAT_TYPE = "task_repeat_type"
    const val COLUMN_TASK_REPEAT_INTERVAL_DAYS = "task_repeat_interval_days"
    const val COLUMN_TASK_REPEAT_INTERVAL_WEEKS = "task_repeat_interval_weeks"
    const val COLUMN_TASK_REPEAT_INTERVAL_MONTHS = "task_repeat_interval_months"
    const val COLUMN_TASK_IS_ACTIVE = "task_is_active"
    const val COLUMN_TASK_REMINDER_MINUTES_BEFORE = "task_reminder_minutes_before"
    const val COLUMN_TASK_CREATED_AT = "task_created_at"
    const val COLUMN_TASK_UPDATED_AT = "task_updated_at"
}