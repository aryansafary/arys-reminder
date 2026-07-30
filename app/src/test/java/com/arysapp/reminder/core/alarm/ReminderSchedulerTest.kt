package com.arysapp.reminder.core.alarm
import android.app.AlarmManager
import android.content.Context
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.domain.usecase.GetAllRemindersUseCase
import com.arysapp.reminder.domain.usecase.ReminderUseCases
import org.junit.Assert

@OptIn(ExperimentalCoroutinesApi::class)
class ReminderSchedulerTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var alarmManager: AlarmManager

    @Mock
    private lateinit var reminderUseCases: ReminderUseCases

    @Mock
    private lateinit var getAllRemindersUseCase: GetAllRemindersUseCase

    private lateinit var reminderScheduler: ReminderScheduler

    private lateinit var autoCloseable: AutoCloseable

    @Before
    fun setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        whenever(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager)
        whenever(context.packageName).thenReturn("com.arysapp.reminder")
        whenever(alarmManager.canScheduleExactAlarms()).thenReturn(true)

        whenever(reminderUseCases.getAllReminders).thenReturn(getAllRemindersUseCase)

        reminderScheduler = ReminderScheduler(context, reminderUseCases)
    }

    @After
    fun tearDown() {
        autoCloseable.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `when active reminders fetched, scheduler processes them without exception`() = runTest {
        // Given
        val mockReminders = listOf(
            ReminderModel(
                id = 1L,
                title = "Test",
                description = "Test",
                dateTime = "2028-12-31",
                hourTime = "10:00",
                isActive = true,
                repeatType = RepeatType.NONE.name,
                createdAt = "",
                updatedAt = ""
            )
        )
        whenever(getAllRemindersUseCase()).thenReturn(flowOf(mockReminders))

        // When & Then
        try {
            reminderScheduler.scheduleAllReminders()
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(true).isTrue()
        } catch (e: Exception) {
            Assert.fail("Scheduling failed with exception: ${e.message}")
        }
    }
}