package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.repository.ReminderRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetAllRemindersUseCaseTest {

    private lateinit var useCase: GetAllRemindersUseCase
    private val repository: ReminderRepository = mock()

    @Before
    fun setup() {
        useCase = GetAllRemindersUseCase(repository)
    }

    @Test
    fun `getAllReminders should return list of reminders from repository`() = runBlocking {
        // Arrange
        val mockList = listOf(
            ReminderModel(id = 1, title = "test one" , createdAt = "" , updatedAt = ""),
            ReminderModel(id = 2, title = "test two" , createdAt = "" , updatedAt = "")
        )

        whenever(repository.getAllReminders()).thenReturn(flowOf(mockList))

        // Act
        val result = useCase.invoke().first()

        // Assert
        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("test one")
    }
}