package com.arysapp.reminder.core.alarm
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.arysapp.reminder.domain.model.ReminderModel
object AlarmIntentFactory {

    const val EXTRA_REMINDER_ID = "reminder_id"
    const val EXTRA_REMINDER_TITLE = "reminder_title"
    const val EXTRA_REMINDER_DESCRIPTION = "reminder_description"
    const val EXTRA_REPEAT_TYPE = "repeat_type"
    const val EXTRA_DATE_TIME = "dateTime"
    const val EXTRA_HOUR_TIME = "hourTime"
    const val EXTRA_IS_REMINDER_ONLY = "is_reminder_only"
    const val EXTRA_REMINDER_INDEX = "reminder_index"
    const val REMINDER_REQUEST_CODE_MULTIPLIER = 100

    fun createReminderPendingIntent(context: Context, reminder: ReminderModel, reminderIndex: Int): PendingIntent {
        val intent = Intent(context, ReminderAlarmReceiver::class.java).apply {
            setPackage(context.packageName)
            putExtra(EXTRA_REMINDER_ID, reminder.id)
            putExtra(EXTRA_REMINDER_TITLE, reminder.title)
            putExtra(EXTRA_REMINDER_DESCRIPTION, reminder.description ?: "")
            putExtra(EXTRA_IS_REMINDER_ONLY, true)
            putExtra(EXTRA_REMINDER_INDEX, reminderIndex)
            putExtra(EXTRA_REPEAT_TYPE, reminder.repeatType)
            putExtra(EXTRA_DATE_TIME, reminder.dateTime ?: "")
            putExtra(EXTRA_HOUR_TIME, reminder.hourTime ?: "")
        }

        val requestCode = (reminder.id % Int.MAX_VALUE).toInt() * REMINDER_REQUEST_CODE_MULTIPLIER + reminderIndex

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun createDuePendingIntent(context: Context, reminder: ReminderModel): PendingIntent {
        val intent = Intent(context, ReminderAlarmReceiver::class.java).apply {
            setPackage(context.packageName)
            putExtra(EXTRA_REMINDER_ID, reminder.id)
            putExtra(EXTRA_REMINDER_TITLE, reminder.title)
            putExtra(EXTRA_REMINDER_DESCRIPTION, reminder.description ?: "")
            putExtra(EXTRA_IS_REMINDER_ONLY, false)
            putExtra(EXTRA_REPEAT_TYPE, reminder.repeatType)
            putExtra(EXTRA_DATE_TIME, reminder.dateTime ?: "")
            putExtra(EXTRA_HOUR_TIME, reminder.hourTime ?: "")
        }

        val requestCode = (reminder.id % Int.MAX_VALUE).toInt()

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}