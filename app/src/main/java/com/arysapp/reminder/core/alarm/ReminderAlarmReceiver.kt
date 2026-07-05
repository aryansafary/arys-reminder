package com.arysapp.reminder.core.alarm

import com.arysapp.reminder.core.notification.ReminderNotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.arysapp.reminder.R

class ReminderAlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "ReminderAlarmReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val isReminderOnly = intent.getBooleanExtra(AlarmIntentFactory.EXTRA_IS_REMINDER_ONLY, true)
            val reminderId = intent.getLongExtra(AlarmIntentFactory.EXTRA_REMINDER_ID, -1L)
            val reminderTitle = intent.getStringExtra(AlarmIntentFactory.EXTRA_REMINDER_TITLE) ?: "تسک"
            val reminderDesc = intent.getStringExtra(AlarmIntentFactory.EXTRA_REMINDER_DESCRIPTION) ?: ""

            Log.d(TAG, "onReceive - reminderId=$reminderId isReminderOnly=$isReminderOnly")

            ReminderNotificationManager.ensureChannelsExist(context)

            if (isReminderOnly) {
                val reminderIndex = intent.getIntExtra("reminder_index", -1)

                val message = when (reminderIndex) {
                    0 -> context.getString(R.string.One_day_until_activity)+" $reminderTitle"
                    1 -> context.getString(R.string.twelve_hour_until_activity)+" $reminderTitle"
                    2 -> context.getString(R.string.One_hour_until_activity)+" $reminderTitle"
                    3 -> context.getString(R.string.Half_hour_until_activity)+" $reminderTitle"
                    4 -> context.getString(R.string.Fifteen_minutes_until_activity)+" $reminderTitle"
                    5 -> context.getString(R.string.One_minutes_until_activity)+" $reminderTitle"
                    else -> context.getString(R.string.At_time_of_activity)+" $reminderTitle"
                }
                ReminderNotificationManager.showReminderNotification(
                    context,
                    context.getString(R.string.Reminder)+" $reminderTitle",
                    message,
                    reminderId
                )
            } else {
                val alarmIntent = Intent(context, AlarmRingtoneService::class.java).apply {
                    putExtra(AlarmIntentFactory.EXTRA_REMINDER_ID, reminderId)
                    putExtra(AlarmIntentFactory.EXTRA_REMINDER_TITLE, reminderTitle)
                    putExtra(AlarmIntentFactory.EXTRA_REMINDER_DESCRIPTION, reminderDesc)
                    putExtra(AlarmIntentFactory.EXTRA_REPEAT_TYPE, intent.getStringExtra(AlarmIntentFactory.EXTRA_REPEAT_TYPE))
                    putExtra(AlarmIntentFactory.EXTRA_DATE_TIME, intent.getStringExtra(AlarmIntentFactory.EXTRA_DATE_TIME))
                    putExtra(AlarmIntentFactory.EXTRA_HOUR_TIME, intent.getStringExtra(AlarmIntentFactory.EXTRA_HOUR_TIME))
                    setPackage(context.packageName)
                }

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(alarmIntent)
                    } else {
                        context.startService(alarmIntent)
                    }
                    Log.d(TAG, "Started AlarmRingtoneService for reminderId=$reminderId")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to start AlarmRingtoneService", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "ReminderAlarmReceiver.onReceive error", e)
        }
    }
}
