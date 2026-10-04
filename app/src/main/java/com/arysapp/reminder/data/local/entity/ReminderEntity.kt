package com.arysapp.reminder.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_CREATED_AT
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_DESCRIPTION
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_HOUR_TIME
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_ID
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_IS_ACTIVE
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_REMINDER_MINUTES_BEFORE
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_REPEAT_INTERVAL_DAYS
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_REPEAT_INTERVAL_MONTHS
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_REPEAT_INTERVAL_WEEKS
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_REPEAT_TYPE
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_DATE_TIME
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_REPEAT_INTERVAL_YEARS
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_TITLE
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_UPDATED_AT
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_CATEGORY
import com.arysapp.reminder.utils.ConstantsDatabase.REMINDER_TABLE_NAME

@Entity(tableName = REMINDER_TABLE_NAME)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_REMINDER_ID)
    val id: Long = 0,

    @ColumnInfo(name = COLUMN_REMINDER_TITLE)
    val title: String,

    @ColumnInfo(name = COLUMN_REMINDER_DESCRIPTION)
    val description: String? = null,

    @ColumnInfo(name = COLUMN_REMINDER_DATE_TIME)
    val dateTime: String? = null,

    @ColumnInfo(name = COLUMN_REMINDER_HOUR_TIME)
    val hourTime: String? = null,

    @ColumnInfo(name = COLUMN_REMINDER_REPEAT_TYPE)
    val repeatType: String = RepeatType.NONE.name,

    @ColumnInfo(name = COLUMN_REMINDER_REPEAT_INTERVAL_DAYS)
    val repeatIntervalDays: Int? = null,

    @ColumnInfo(name = COLUMN_REMINDER_REPEAT_INTERVAL_WEEKS)
    val repeatIntervalWeeks: Int? = null,

    @ColumnInfo(name = COLUMN_REMINDER_REPEAT_INTERVAL_MONTHS)
    val repeatIntervalMonths: Int? = null,

    @ColumnInfo(name = COLUMN_REMINDER_REPEAT_INTERVAL_YEARS)
    val repeatIntervalYears: Int? = null,

    @ColumnInfo(name = COLUMN_REMINDER_IS_ACTIVE)
    val isActive: Boolean = true,

    @ColumnInfo(name = COLUMN_REMINDER_REMINDER_MINUTES_BEFORE)
    val reminderMinutesBefore: Int? = null,

    // فیلد جدید دسته‌بندی با مقدار پیش‌فرض عمومی
    @ColumnInfo(name = COLUMN_REMINDER_CATEGORY)
    val category: String = "GENERAL",

    @ColumnInfo(name = COLUMN_REMINDER_CREATED_AT)
    val createdAt: String,

    @ColumnInfo(name = COLUMN_REMINDER_UPDATED_AT)
    val updatedAt: String
)