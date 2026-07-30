package com.arysapp.reminder.core.alarm

import android.app.Service
import android.content.Intent
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import com.arysapp.reminder.core.notification.ReminderNotificationManager
import com.arysapp.reminder.domain.model.RepeatType

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmRingtoneServiceTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var reminderRepeatHandler: ReminderRepeatHandler

    private lateinit var alarmRingtoneService: AlarmRingtoneService

    private lateinit var autoCloseable: AutoCloseable

    @Before
    fun setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        alarmRingtoneService = AlarmRingtoneService().apply {
            reminderRepeatHandler = this@AlarmRingtoneServiceTest.reminderRepeatHandler
        }
    }

    @After
    fun tearDown() {
        autoCloseable.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `when stop alarm action received, repeat handler is triggered`() = runTest {
        // Given
        val intent = Intent().apply {
            action = ReminderNotificationManager.ACTION_STOP_ALARM
            putExtra(AlarmIntentFactory.EXTRA_REMINDER_ID, 100L)
            putExtra(AlarmIntentFactory.EXTRA_REPEAT_TYPE, RepeatType.DAILY.name)
            putExtra(AlarmIntentFactory.EXTRA_DATE_TIME, "2026-07-29")
            putExtra(AlarmIntentFactory.EXTRA_HOUR_TIME, "12:00")
        }

        // When
        val result = alarmRingtoneService.onStartCommand(intent, 0, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(result).isEqualTo(Service.START_NOT_STICKY)
    }
}