package com.arysapp.reminder.core.alarm

import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.domain.usecase.DeleteReminderUseCase
import com.arysapp.reminder.domain.usecase.ProcessExpiredRemindersUseCase
import com.arysapp.reminder.domain.usecase.ReminderUseCases
import com.arysapp.reminder.domain.usecase.UpdateReminderDateTimeUseCase
import com.arysapp.reminder.domain.usecase.UpdateReminderStatusUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ReminderRepeatHandlerTest {

    private lateinit var reminderUseCases: ReminderUseCases
    private lateinit var updateReminderDateTimeUseCase: UpdateReminderDateTimeUseCase
    private lateinit var deleteReminderUseCase: DeleteReminderUseCase
    private lateinit var updateReminderStatusUseCase: UpdateReminderStatusUseCase
    private lateinit var processExpiredRemindersUseCase: ProcessExpiredRemindersUseCase

    private lateinit var reminderScheduler: ReminderScheduler
    private lateinit var repeatHandler: ReminderRepeatHandler

    @Before
    fun setUp() {
        reminderUseCases = mock()
        updateReminderDateTimeUseCase = mock()
        deleteReminderUseCase = mock()
        updateReminderStatusUseCase = mock()
        processExpiredRemindersUseCase = mock()


        whenever(reminderUseCases.updateReminderDateTime).thenReturn(updateReminderDateTimeUseCase)
        whenever(reminderUseCases.deleteReminder).thenReturn(deleteReminderUseCase)
        whenever(reminderUseCases.updateReminderStatus).thenReturn(updateReminderStatusUseCase)
        whenever(reminderUseCases.processExpiredReminders).thenReturn(processExpiredRemindersUseCase)

        reminderScheduler = mock()
        repeatHandler = ReminderRepeatHandler(reminderUseCases, reminderScheduler)
    }

    @Test
    fun handleRepeat_with_DAILY_repeat_type_should_increment_date_by_one_day() = runTest {
        // Given
        val reminderId = 1L
        val repeatType = RepeatType.DAILY.name
        val initialDate = "2026-08-01"
        val hourTime = "10:00"

        // When
        repeatHandler.handleRepeat(reminderId, repeatType, initialDate, hourTime)

        // Then
        verify(updateReminderDateTimeUseCase).invoke(reminderId, "2026-08-02", hourTime)
        verify(reminderScheduler).cancelReminderForReminder(reminderId)
        verify(reminderScheduler).scheduleAllReminders()
    }

    @Test
    fun handleRepeat_with_WEEKLY_repeat_type_should_increment_date_by_seven_days() = runTest {
        // Given
        val reminderId = 2L
        val repeatType = RepeatType.WEEKLY.name
        val initialDate = "2026-08-01"
        val hourTime = "12:00"

        // When
        repeatHandler.handleRepeat(reminderId, repeatType, initialDate, hourTime)

        // Then
        verify(updateReminderDateTimeUseCase).invoke(reminderId, "2026-08-08", hourTime)
        verify(reminderScheduler).cancelReminderForReminder(reminderId)
        verify(reminderScheduler).scheduleAllReminders()
    }

    @Test
    fun handleRepeat_with_MONTHLY_repeat_type_should_increment_date_by_one_month() = runTest {
        // Given
        val reminderId = 3L
        val repeatType = RepeatType.MONTHLY.name
        val initialDate = "2026-08-15"
        val hourTime = "08:30"

        // When
        repeatHandler.handleRepeat(reminderId, repeatType, initialDate, hourTime)

        // Then
        verify(updateReminderDateTimeUseCase).invoke(reminderId, "2026-09-15", hourTime)
        verify(reminderScheduler).cancelReminderForReminder(reminderId)
        verify(reminderScheduler).scheduleAllReminders()
    }

    @Test
    fun handleRepeat_with_NONE_repeat_type_should_delete_reminder() = runTest {
        // Given
        val reminderId = 4L
        val repeatType = RepeatType.NONE.name
        val initialDate = "2026-08-01"
        val hourTime = "14:00"

        // When
        repeatHandler.handleRepeat(reminderId, repeatType, initialDate, hourTime)

        // Then
        verify(deleteReminderUseCase).invoke(any())
        verify(reminderScheduler).cancelReminderForReminder(reminderId)
        verify(reminderScheduler).scheduleAllReminders()
    }

    @Test
    fun handleRepeat_with_YEARLY_repeat_type_should_increment_date_by_one_year() = runTest {
        // Given
        val reminderId = 5L
        val repeatType = RepeatType.YEARLY.name
        val initialDate = "2026-08-01"
        val hourTime = "09:00"

        // When
        repeatHandler.handleRepeat(reminderId, repeatType, initialDate, hourTime)

        // Then
        // 2026-08-01 + 1 year = 2027-08-01
        verify(updateReminderDateTimeUseCase).invoke(reminderId, "2027-08-01", hourTime)
        verify(reminderScheduler).cancelReminderForReminder(reminderId)
        verify(reminderScheduler).scheduleAllReminders()
    }
}