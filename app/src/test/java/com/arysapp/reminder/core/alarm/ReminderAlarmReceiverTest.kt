package com.arysapp.reminder.core.alarm

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.arysapp.reminder.domain.model.RepeatType
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ReminderAlarmReceiverTest {

    private lateinit var context: Context
    private lateinit var receiver: ReminderAlarmReceiver

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        receiver = ReminderAlarmReceiver()
    }

    @Test
    fun `onReceive with isReminderOnly true should execute without crashing`() {
        // Given
        val intent = Intent(context, ReminderAlarmReceiver::class.java).apply {
            putExtra(AlarmIntentFactory.EXTRA_IS_REMINDER_ONLY, true)
            putExtra(AlarmIntentFactory.EXTRA_REMINDER_ID, 100L)
            putExtra(AlarmIntentFactory.EXTRA_REMINDER_TITLE, "Test")
            putExtra(AlarmIntentFactory.EXTRA_REMINDER_DESCRIPTION, "Test description")
            putExtra("reminder_index", 4)
        }

        // When & Then (Ensuring no exception is thrown during execution)
        var exceptionThrown = false
        try {
            receiver.onReceive(context, intent)
        } catch (_: Exception) {
            exceptionThrown = true
        }

        assertThat(exceptionThrown).isFalse()
    }

    @Test
    fun `onReceive with isReminderOnly false should handle service intent without crashing`() {
        // Given
        val intent = Intent(context, ReminderAlarmReceiver::class.java).apply {
            putExtra(AlarmIntentFactory.EXTRA_IS_REMINDER_ONLY, false)
            putExtra(AlarmIntentFactory.EXTRA_REMINDER_ID, 200L)
            putExtra(AlarmIntentFactory.EXTRA_REMINDER_TITLE, "زمان هشدار")
            putExtra(AlarmIntentFactory.EXTRA_REMINDER_DESCRIPTION, "زنگ هشدار فعال شد")
            putExtra(AlarmIntentFactory.EXTRA_REPEAT_TYPE, RepeatType.NONE.name)
        }

        // When & Then
        var exceptionThrown = false
        try {
            receiver.onReceive(context, intent)
        } catch (_: Exception) {
            exceptionThrown = true
        }

        assertThat(exceptionThrown).isFalse()
    }
}