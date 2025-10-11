package com.arysapp.task.core.alarm

import com.arysapp.task.core.notification.TaskNotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.arysapp.task.R

class TaskAlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "TaskAlarmReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val isReminderOnly = intent.getBooleanExtra(AlarmIntentFactory.EXTRA_IS_REMINDER_ONLY, true)
            val taskId = intent.getLongExtra(AlarmIntentFactory.EXTRA_TASK_ID, -1L)
            val taskTitle = intent.getStringExtra(AlarmIntentFactory.EXTRA_TASK_TITLE) ?: "تسک"
            val taskDesc = intent.getStringExtra(AlarmIntentFactory.EXTRA_TASK_DESCRIPTION) ?: ""

            Log.d(TAG, "onReceive - taskId=$taskId isReminderOnly=$isReminderOnly")

            TaskNotificationManager.ensureChannelsExist(context)

            if (isReminderOnly) {
                val reminderIndex = intent.getIntExtra("reminder_index", -1)

                val message = when (reminderIndex) {
                    0 -> context.getString(R.string.One_day_until_activity)+" $taskTitle"
                    1 -> context.getString(R.string.twelve_hour_until_activity)+" $taskTitle"
                    2 -> context.getString(R.string.One_hour_until_activity)+" $taskTitle"
                    3 -> context.getString(R.string.Half_hour_until_activity)+" $taskTitle"
                    4 -> context.getString(R.string.Fifteen_minutes_until_activity)+" $taskTitle"
                    5 -> context.getString(R.string.One_minutes_until_activity)+" $taskTitle"
                    else -> context.getString(R.string.At_time_of_activity)+" $taskTitle"
                }
                TaskNotificationManager.showReminderNotification(
                    context,
                    context.getString(R.string.Reminder)+" $taskTitle",
                    message,
                    taskId
                )
            } else {
                val alarmIntent = Intent(context, AlarmRingtoneService::class.java).apply {
                    putExtra(AlarmIntentFactory.EXTRA_TASK_ID, taskId)
                    putExtra(AlarmIntentFactory.EXTRA_TASK_TITLE, taskTitle)
                    putExtra(AlarmIntentFactory.EXTRA_TASK_DESCRIPTION, taskDesc)
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
                    Log.d(TAG, "Started AlarmRingtoneService for taskId=$taskId")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to start AlarmRingtoneService", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "TaskAlarmReceiver.onReceive error", e)
        }
    }
}
