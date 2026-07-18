package com.arysapp.reminder.domain.usecase
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.repository.ReminderRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DeleteReminderUseCaseTest {

    private lateinit var deleteReminderUseCase: DeleteReminderUseCase
    private val repository: ReminderRepository = mock() // شبیه‌سازی مخزن

    @Before
    fun setup() {
        deleteReminderUseCase = DeleteReminderUseCase(repository)
    }

    @Test
    fun `delete reminder should call repository delete method`() = runBlocking {
        // Arrange
        val reminder = ReminderModel(
            id = 1,
            title = "test",
            createdAt = "",
            updatedAt = ""
        )
        whenever(repository.deleteReminder(reminder)).thenReturn(1)

        // Act
        val result = deleteReminderUseCase(reminder)

        // Assert
        verify(repository).deleteReminder(reminder)
        assertThat(result.isSuccess).isTrue()
    }
}