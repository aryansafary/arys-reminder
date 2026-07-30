package com.arysapp.reminder.core.notification

import android.app.NotificationManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ReminderNotificationManagerTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun ensureChannelsExist_should_create_notification_channels() {
        // When
        ReminderNotificationManager.ensureChannelsExist(context)

        // Then
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val reminderChannel = notificationManager.getNotificationChannel(ReminderNotificationManager.CHANNEL_ID_REMINDER)
        val alarmChannel = notificationManager.getNotificationChannel(ReminderNotificationManager.CHANNEL_ID_ALARM)

        assertThat(reminderChannel).isNotNull()
        assertThat(reminderChannel?.id).isEqualTo(ReminderNotificationManager.CHANNEL_ID_REMINDER)

        assertThat(alarmChannel).isNotNull()
        assertThat(alarmChannel?.id).isEqualTo(ReminderNotificationManager.CHANNEL_ID_ALARM)
    }

    @Test
    fun createStopPendingIntent_should_return_valid_pending_intent() {
        // Given
        val reminderId = 123L
        val repeatType = "DAILY"
        val dateTime = "2026-08-01"
        val hourTime = "12:00"

        // When
        val pendingIntent = ReminderNotificationManager.createStopPendingIntent(
            context = context,
            reminderId = reminderId,
            repeatType = repeatType,
            dateTime = dateTime,
            hourTime = hourTime
        )

        // Then
        assertThat(pendingIntent).isNotNull()
    }

    @Test
    fun buildAlarmForegroundNotification_should_return_built_notification() {
        // Given
        val title = "یادآور مهم"
        val message = "زمان انجام کار فرا رسیده است"
        val reminderId = 456L
        val notificationId = 1001

        val stopPendingIntent = ReminderNotificationManager.createStopPendingIntent(
            context = context,
            reminderId = reminderId,
            repeatType = "NONE",
            dateTime = null,
            hourTime = null
        )

        // When
        val notification = ReminderNotificationManager.buildAlarmForegroundNotification(
            context = context,
            title = title,
            message = message,
            stopPendingIntent = stopPendingIntent,
            notificationId = notificationId,
            reminderId = reminderId
        )

        // Then
        assertThat(notification).isNotNull()
        assertThat(notification.extras.getString(android.app.Notification.EXTRA_TITLE)).isEqualTo(title)
        assertThat(notification.extras.getString(android.app.Notification.EXTRA_TEXT)).isEqualTo(message)
    }

    @Test
    fun getDefaultAlarmSound_should_return_valid_uri() {
        // When
        val uri = ReminderNotificationManager.getDefaultAlarmSound()

        // Then
        assertThat(uri).isNotNull()
    }
}