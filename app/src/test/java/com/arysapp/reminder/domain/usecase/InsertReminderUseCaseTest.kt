package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.repository.ReminderRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class InsertReminderUseCaseTest {

    private lateinit var useCase: InsertReminderUseCase
    private val repository: ReminderRepository = mock()

    @Before
    fun setup() {
        useCase = InsertReminderUseCase(repository)
    }

    @Test
    fun `when valid reminder is provided, should pass correct model to repository`() = runBlocking {
        // Arrange
        val testTitle = "تست عنوان"
        val testDesc = "تست توضیحات"

        whenever(repository.insertReminder(any())).thenReturn(1L)

        // Act
        useCase.invoke(title = testTitle, description = testDesc)

        // Assert
        val captor = argumentCaptor<ReminderModel>()

        verify(repository).insertReminder(captor.capture())


        val capturedReminder = captor.firstValue
        assertThat(capturedReminder.title).isEqualTo(testTitle)
        assertThat(capturedReminder.description).isEqualTo(testDesc)
        assertThat(capturedReminder.isActive).isTrue()
    }
}