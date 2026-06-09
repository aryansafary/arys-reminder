package com.arysapp.task.core.alarm
import com.arysapp.task.domain.model.TaskModel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object AlarmIntentFactory {

    const val EXTRA_TASK_ID = "task_id"
    const val EXTRA_TASK_TITLE = "task_title"
    const val EXTRA_TASK_DESCRIPTION = "task_description"
    const val EXTRA_REPEAT_TYPE = "repeat_type"
    const val EXTRA_DATE_TIME = "dateTime"
    const val EXTRA_HOUR_TIME = "hourTime"
    const val EXTRA_IS_REMINDER_ONLY = "is_reminder_only"
    const val EXTRA_REMINDER_INDEX = "reminder_index"
    const val REMINDER_REQUEST_CODE_MULTIPLIER = 100

    fun createReminderPendingIntent(context: Context, task: TaskModel, reminderIndex: Int): PendingIntent {
        val intent = Intent(context, TaskAlarmReceiver::class.java).apply {
            setPackage(context.packageName)
            putExtra(EXTRA_TASK_ID, task.id)
            putExtra(EXTRA_TASK_TITLE, task.title)
            putExtra(EXTRA_TASK_DESCRIPTION, task.description ?: "")
            putExtra(EXTRA_IS_REMINDER_ONLY, true)
            putExtra(EXTRA_REMINDER_INDEX, reminderIndex)
            putExtra(EXTRA_REPEAT_TYPE, task.repeatType)
            putExtra(EXTRA_DATE_TIME, task.dateTime)
            putExtra(EXTRA_HOUR_TIME, task.hourTime)
        }

        val requestCode = task.id.toInt() * REMINDER_REQUEST_CODE_MULTIPLIER + reminderIndex
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun createDuePendingIntent(context: Context, task: TaskModel): PendingIntent {
        val intent = Intent(context, TaskAlarmReceiver::class.java).apply {
            setPackage(context.packageName)
            putExtra(EXTRA_TASK_ID, task.id)
            putExtra(EXTRA_TASK_TITLE, task.title)
            putExtra(EXTRA_TASK_DESCRIPTION, task.description ?: "")
            putExtra(EXTRA_IS_REMINDER_ONLY, false)
            putExtra(EXTRA_REPEAT_TYPE, task.repeatType)
            putExtra(EXTRA_DATE_TIME, task.dateTime)
            putExtra(EXTRA_HOUR_TIME, task.hourTime)
        }

        val requestCode = task.id.toInt()
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
