package com.arysapp.reminder.core.alarm

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import com.arysapp.reminder.R
import com.arysapp.reminder.core.notification.ReminderNotificationManager
import com.arysapp.reminder.utils.Constants.USER_LANGUAGE
import java.util.Locale

class ReminderAlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "ReminderAlarmReceiver"
    }

    private fun getLocalizedContext(context: Context): Context {
        val lang = USER_LANGUAGE ?: "fa"
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
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

                val localizedContext = getLocalizedContext(context)

                val message = when (reminderIndex) {
                    0 -> localizedContext.getString(R.string.one_month_until_activity) + " $reminderTitle"
                    1 -> localizedContext.getString(R.string.fifteen_days_until_activity) + " $reminderTitle"
                    2 -> localizedContext.getString(R.string.one_week_until_activity) + " $reminderTitle"
                    3 -> localizedContext.getString(R.string.three_days_until_activity) + " $reminderTitle"
                    4 -> localizedContext.getString(R.string.one_day_until_activity) + " $reminderTitle"
                    5 -> localizedContext.getString(R.string.twelve_hour_until_activity) + " $reminderTitle"
                    6 -> localizedContext.getString(R.string.six_hour_until_activity) + " $reminderTitle"
                    7 -> localizedContext.getString(R.string.three_hour_until_activity) + " $reminderTitle"
                    8 -> localizedContext.getString(R.string.one_hour_until_activity) + " $reminderTitle"
                    9 -> localizedContext.getString(R.string.half_hour_until_activity) + " $reminderTitle"
                    10 -> localizedContext.getString(R.string.fifteen_minutes_until_activity) + " $reminderTitle"
                    else -> localizedContext.getString(R.string.at_time_of_activity) + " $reminderTitle"
                }

                val title = localizedContext.getString(R.string.Reminder)
                //+ " $reminderTitle"

                ReminderNotificationManager.showReminderNotification(
                    context,
                    title,
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