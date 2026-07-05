package com.arysapp.reminder.core.notification
import android.Manifest
import android.annotation.SuppressLint
import com.arysapp.reminder.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arysapp.reminder.MainActivity
import com.arysapp.reminder.core.alarm.AlarmIntentFactory
import com.arysapp.reminder.core.alarm.AlarmRingtoneService

object ReminderNotificationManager {

    const val CHANNEL_ID_REMINDER = "reminder_channel"
    const val CHANNEL_ID_ALARM = "alarm_channel"

    private const val REMINDER_ID_OFFSET = 10_000
    const val ACTION_STOP_ALARM = "com.arysapp.reminder.action.STOP_ALARM"

    fun ensureChannelsExist(context: Context) {
        ensureReminderChannelExists(context)
        ensureAlarmChannelExists(context)
    }

    private fun ensureReminderChannelExists(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(NotificationManager::class.java)
            if (nm.getNotificationChannel(CHANNEL_ID_REMINDER) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID_REMINDER,
                    context.getString(R.string.Task_Reminder),
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = context.getString(R.string.Task_Reminder)
                }
                nm.createNotificationChannel(channel)
            }
        }
    }

    private fun ensureAlarmChannelExists(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(NotificationManager::class.java)
            if (nm.getNotificationChannel(CHANNEL_ID_ALARM) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID_ALARM,
                    context.getString(R.string.reminder_alarm),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = context.getString(R.string.alarm_notif)
                    setSound(null, null)
                    enableVibration(true)
                }
                nm.createNotificationChannel(channel)
            }
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showReminderNotification(context: Context, title: String, message: String, reminderId: Long) {
        ensureReminderChannelExists(context)

        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pending = PendingIntent.getActivity(
            context,
            reminderId.toInt(),
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notif = NotificationCompat.Builder(context, CHANNEL_ID_REMINDER)
            .setSmallIcon(R.drawable.calendar_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(sound)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pending)
            .build()

        NotificationManagerCompat.from(context).notify((reminderId.toInt()) + REMINDER_ID_OFFSET, notif)
    }

    fun createStopPendingIntent(
        context: Context,
        reminderId: Long,
        repeatType: String?,
        dateTime: String?,
        hourTime: String?
    ): PendingIntent {
        val stopIntent = Intent(context, AlarmRingtoneService::class.java).apply {
            action = ACTION_STOP_ALARM
            putExtra(AlarmIntentFactory.EXTRA_REMINDER_ID, reminderId)
            putExtra(AlarmIntentFactory.EXTRA_REPEAT_TYPE, repeatType)
            putExtra(AlarmIntentFactory.EXTRA_DATE_TIME, dateTime)
            putExtra(AlarmIntentFactory.EXTRA_HOUR_TIME, hourTime)
            setPackage(context.packageName)
        }

        val req = if (reminderId != -1L) reminderId.toInt() else 0
        return PendingIntent.getService(
            context,
            req,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    @SuppressLint("FullScreenIntentPolicy")
    fun buildAlarmForegroundNotification(
        context: Context,
        title: String,
        message: String,
        stopPendingIntent: PendingIntent,
        notificationId: Int,
        reminderId: Long
    ): Notification {
        ensureAlarmChannelExists(context)

        // این اینتنت اکتیویتی اصلی را باز می‌کند
        val fullScreenIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("OPEN_SCREEN", "SHOW_ALARM")
            putExtra("TASK_ID", reminderId)
            putExtra("TASK_TITLE", title)
            putExtra("TASK_DESC", message)
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_ID_ALARM)
            .setSmallIcon(R.drawable.time_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(false)
            .addAction(R.drawable.settings_outlined, context.getString(R.string.stop), stopPendingIntent)
            .build()
    }

    fun getDefaultAlarmSound(): Uri =
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM) ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
}
