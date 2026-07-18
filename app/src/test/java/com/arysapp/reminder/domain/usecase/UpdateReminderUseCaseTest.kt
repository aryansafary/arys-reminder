package com.arysapp.reminder.domain.usecase
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.domain.repository.ReminderRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UpdateReminderUseCaseTest {

    private lateinit var useCase: UpdateReminderUseCase
    private val repository: ReminderRepository = mock()

    @Before
    fun setup() {
        useCase = UpdateReminderUseCase(repository)
    }

    @Test
    fun `when title is blank, should return failure`() = runBlocking {
        val reminder = ReminderModel(title = "", repeatType = RepeatType.NONE.name , createdAt = "", updatedAt = "")

        val result = useCase.invoke(reminder)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `when repeat type is invalid, should return failure`() = runBlocking {
        val reminder = ReminderModel(title = "Title", repeatType = "INVALID_TYPE", createdAt = "", updatedAt = "")

        val result = useCase.invoke(reminder)

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `when repeat type is not NONE but intervals are null, should return failure`() = runBlocking {
        val reminder = ReminderModel(
            title = "Title",
            repeatType = RepeatType.DAILY.name,
            repeatIntervalDays = null,
            repeatIntervalWeeks = null,
            repeatIntervalMonths = null,
            repeatIntervalYears = null,
            createdAt = "",
            updatedAt = ""
        )

        val result = useCase.invoke(reminder)

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `when valid reminder is provided, should call repository and return success`(): Unit = runBlocking {
        // Arrange
        val reminder = ReminderModel(
            id = 1,
            title = "Valid Title",
            repeatType = RepeatType.NONE.name,
            createdAt = "",
            updatedAt = ""
        )
        whenever(repository.updateReminder(any())).thenReturn(1)

        // Act
        val result = useCase.invoke(reminder)

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(1)
        verify(repository).updateReminder(any())
    }
}