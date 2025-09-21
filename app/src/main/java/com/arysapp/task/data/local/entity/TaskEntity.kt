package com.arysapp.task.data.local.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arysapp.task.domain.model.RepeatType
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_CREATED_AT
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_DESCRIPTION
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_HOUR_TIME
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_ID
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_IS_ACTIVE
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_REMINDER_MINUTES_BEFORE
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_REPEAT_INTERVAL_DAYS
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_REPEAT_INTERVAL_MONTHS
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_REPEAT_INTERVAL_WEEKS
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_REPEAT_TYPE
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_DATE_TIME
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_TITLE
import com.arysapp.task.utils.ConstantsDatabase.COLUMN_TASK_UPDATED_AT
import com.arysapp.task.utils.ConstantsDatabase.TASK_TABLE_NAME

@Entity(tableName = TASK_TABLE_NAME)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_TASK_ID)
    val id: Long = 0,

    @ColumnInfo(name = COLUMN_TASK_TITLE)
    val title: String,

    @ColumnInfo(name = COLUMN_TASK_DESCRIPTION)
    val description: String? = null,

    @ColumnInfo(name = COLUMN_TASK_DATE_TIME)
    val dateTime: String? = null,

    @ColumnInfo(name = COLUMN_TASK_HOUR_TIME)
    val hourTime: String? = null,

    @ColumnInfo(name = COLUMN_TASK_REPEAT_TYPE)
    val repeatType: String = RepeatType.NONE.name,

    @ColumnInfo(name = COLUMN_TASK_REPEAT_INTERVAL_DAYS)
    val repeatIntervalDays: Int? = null,

    @ColumnInfo(name = COLUMN_TASK_REPEAT_INTERVAL_WEEKS)
    val repeatIntervalWeeks: Int? = null,

    @ColumnInfo(name = COLUMN_TASK_REPEAT_INTERVAL_MONTHS)
    val repeatIntervalMonths: Int? = null,

    @ColumnInfo(name = COLUMN_TASK_IS_ACTIVE)
    val isActive: Boolean = true,

    @ColumnInfo(name = COLUMN_TASK_REMINDER_MINUTES_BEFORE)
    val reminderMinutesBefore: Int? = null,

    @ColumnInfo(name = COLUMN_TASK_CREATED_AT)
    val createdAt: String,

    @ColumnInfo(name = COLUMN_TASK_UPDATED_AT)
    val updatedAt: String
)

