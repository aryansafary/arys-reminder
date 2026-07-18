package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.repository.ReminderRepository
import com.arysapp.reminder.domain.model.RepeatType
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify

class ProcessExpiredRemindersUseCaseTest {

    private lateinit var useCase: ProcessExpiredRemindersUseCase
    private val repository: ReminderRepository = mock()

    @Before
    fun setup() {
        useCase = ProcessExpiredRemindersUseCase(repository)
    }

    @Test
    fun `when reminder is expired and repeat type is NONE, should delete it`(): Unit = runBlocking {
        // Arrange
        val expiredReminder = ReminderModel(
            id = 1,
            title = "Expired Reminder",
            description = "This reminder is expired",
            dateTime = "2000-01-01",
            hourTime = "10:00",
            repeatType = RepeatType.NONE.name,
            createdAt = "",
            updatedAt = ""
        )
        whenever(repository.getAllRemindersOnce()).thenReturn(listOf(expiredReminder))

        // Act
        useCase.invoke()

        // Assert
        verify(repository).deleteReminder(expiredReminder)
    }
}