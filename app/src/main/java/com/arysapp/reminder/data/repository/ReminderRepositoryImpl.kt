package com.arysapp.reminder.data.repository

import com.arysapp.reminder.data.local.dao.ReminderDao
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.repository.ReminderRepository
import com.arysapp.reminder.utils.mapper.toDomain
import com.arysapp.reminder.utils.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository {

    override fun getAllReminders(): Flow<List<ReminderModel>> {
        return reminderDao.getAllReminder().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getActiveReminders(): Flow<List<ReminderModel>> {
        return reminderDao.getActiveReminder().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getReminderById(id: Long): ReminderModel? {
        return reminderDao.getReminderById(id)?.toDomain()
    }

    override suspend fun insertReminder(reminder: ReminderModel): Long {
        return reminderDao.insertReminder(reminder.toEntity())
    }

    override suspend fun insertReminders(reminders: List<ReminderModel>): List<Long> {
        return reminderDao.insertReminders(reminders.map { it.toEntity() })
    }

    override suspend fun updateReminder(reminder: ReminderModel): Int {
        return reminderDao.updateReminder(reminder.toEntity())
    }

    override suspend fun deleteReminder(reminder: ReminderModel): Int {
        return reminderDao.deleteReminder(reminder.toEntity())
    }

    override suspend fun deleteAllReminders(): Int = reminderDao.deleteAll()

    override suspend fun deleteReminderById(id: Long): Int {
        return reminderDao.deleteReminderById(id)
    }

    override suspend fun updateReminderStatus(id: Long, isActive: Boolean): Int {
        return reminderDao.updateReminderStatus(id, isActive)
    }


    override suspend fun updateReminderDateTime(id: Long, newDateTime: String, newHourTime: String?): Int {
        return reminderDao.updateReminderDateTime(id, newDateTime, newHourTime)
    }

    override suspend fun getAllRemindersOnce(): List<ReminderModel> {
        return reminderDao.getAllRemindersOnce().map { it.toDomain() }
    }

}