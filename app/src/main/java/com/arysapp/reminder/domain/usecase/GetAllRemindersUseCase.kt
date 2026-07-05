package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRemindersUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    operator fun invoke(): Flow<List<ReminderModel>> {
        return reminderRepository.getAllTasks()
    }
}