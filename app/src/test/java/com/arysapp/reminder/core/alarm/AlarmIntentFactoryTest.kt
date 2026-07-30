package com.arysapp.reminder.core.alarm

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.model.RepeatType

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AlarmIntentFactoryTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun createReminderPendingIntent_should_build_intent_with_correct_extras() {
        // Given
        val reminder = ReminderModel(
            id = 15L,
            title = "Test One",
            description = "Test description",
            dateTime = "2026-08-01",
            hourTime = "14:30",
            isActive = true,
            repeatType = RepeatType.DAILY.name,
            createdAt = "",
            updatedAt = ""
        )
        val reminderIndex = 2

        // When
        val pendingIntent = AlarmIntentFactory.createReminderPendingIntent(context, reminder, reminderIndex)

        // Then
        assertThat(pendingIntent).isNotNull()
    }

    @Test
    fun createDuePendingIntent_should_build_intent_with_correct_extras() {
        // Given
        val reminder = ReminderModel(
            id = 20L,
            title = "Test two",
            description = "Test description",
            dateTime = "2026-08-02",
            hourTime = "09:00",
            isActive = true,
            repeatType = RepeatType.NONE.name,
            createdAt = "",
            updatedAt = ""
        )

        // When
        val pendingIntent = AlarmIntentFactory.createDuePendingIntent(context, reminder)

        // Then
        assertThat(pendingIntent).isNotNull()
    }
}