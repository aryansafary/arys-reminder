package com.arysapp.task

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "task_reminder_channel",
                this.getString(R.string.Task_Reminder),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "اعلان‌های موعد تسک‌ها"
                enableVibration(true)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)



        }

    }

}