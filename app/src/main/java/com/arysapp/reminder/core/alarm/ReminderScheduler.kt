package com.arysapp.reminder.core.alarm

import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.usecase.ReminderUseCases
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class ReminderScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val reminderUseCases: ReminderUseCases
) {
    companion object {
        private const val TAG = "ReminderScheduler"
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    // مقادیر دقیق میلی‌ثانیه برای زمان‌های جدید (استفاده از L الزامی است تا Overflow نشود)
    private val reminderOffsets = listOf(
        30L * 24 * 60 * 60 * 1000L, // 1 month before (~30 days)
        15L * 24 * 60 * 60 * 1000L, // 15 days before
        7L * 24 * 60 * 60 * 1000L,  // 1 week before
        3L * 24 * 60 * 60 * 1000L,  // 3 days before
        24L * 60 * 60 * 1000L,      // 1 day before
        12L * 60 * 60 * 1000L,      // 12 hours before
        6L * 60 * 60 * 1000L,       // 6 hours before
        3L * 60 * 60 * 1000L,       // 3 hours before
        60L * 60 * 1000L,           // 1 hour before
        30L * 60 * 1000L,           // 30 minutes before
        15L * 60 * 1000L            // 15 minutes before
    )

    suspend fun scheduleAllReminders() {
        try {
            val allReminders = reminderUseCases.getAllReminders().first()
            val remindersToSchedule = allReminders.filter { it.isActive && !it.dateTime.isNullOrBlank() && !it.hourTime.isNullOrBlank() }
            remindersToSchedule.forEach { reminder ->
                try {
                    scheduleReminderForReminder(reminder)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to schedule for reminder id=${reminder.id}", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "scheduleAllReminders error", e)
        }
    }

    private fun scheduleReminderForReminder(reminder: ReminderModel) {
        val dueTime = parseDueTime(reminder) ?: run {
            Log.w(TAG, "parseDueTime returned null for reminder ${reminder.id}")
            return
        }

        val now = System.currentTimeMillis()

        reminderOffsets.forEachIndexed { index, offset ->
            val reminderTime = dueTime - offset
            if (reminderTime > now) {
                val pending = AlarmIntentFactory.createReminderPendingIntent(context, reminder, index)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTime, pending)
                Log.d(TAG, "Scheduled reminder index=$index for reminder=${reminder.id} at=$reminderTime")
            }
        }

        if (dueTime > now) {
            val duePending = AlarmIntentFactory.createDuePendingIntent(context, reminder)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, dueTime, duePending)
            Log.d(TAG, "Scheduled due alarm for reminder=${reminder.id} at=$dueTime")
        }
    }

    fun cancelReminderForReminder(reminderId: Long) {
        try {
            val dueIntent = Intent(context, ReminderAlarmReceiver::class.java).apply { setPackage(context.packageName) }
            val duePending = android.app.PendingIntent.getBroadcast(
                context,
                reminderId.toInt(),
                dueIntent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(duePending)
            duePending.cancel()

            reminderOffsets.forEachIndexed { index, _ ->
                val requestCode = reminderId.toInt() * AlarmIntentFactory.REMINDER_REQUEST_CODE_MULTIPLIER + index
                val reminderIntent = Intent(context, ReminderAlarmReceiver::class.java).apply { setPackage(context.packageName) }
                val reminderPending = android.app.PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    reminderIntent,
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(reminderPending)
                reminderPending.cancel()
            }

            Log.d(TAG, "Cancelled reminders for reminderId=$reminderId")
        } catch (e: Exception) {
            Log.e(TAG, "cancelReminderForReminder error for id=$reminderId", e)
        }
    }

    private fun parseDueTime(reminder: ReminderModel): Long? {
        return try {
            val dateStr = reminder.dateTime!!.trim()
            val combined = "$dateStr ${reminder.hourTime!!.trim()}"
            formatter.parse(combined)?.time
        } catch (e: Exception) {
            Log.e(TAG, "parseDueTime parse error for reminder=${reminder.id}", e)
            null
        }
    }

    fun scheduleRemindersForReminders(reminders: List<ReminderModel>) {
        reminders.forEach { reminder ->
            if (reminder.isActive && !reminder.dateTime.isNullOrBlank() && !reminder.hourTime.isNullOrBlank()) {
                scheduleReminderForReminder(reminder)
            } else {
                cancelReminderForReminder(reminder.id)
            }
        }
    }
}