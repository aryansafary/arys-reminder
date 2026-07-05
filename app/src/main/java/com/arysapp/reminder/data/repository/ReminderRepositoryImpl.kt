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

    override fun getAllTasks(): Flow<List<ReminderModel>> {
        return reminderDao.getAllReminder().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getActiveTasks(): Flow<List<ReminderModel>> {
        return reminderDao.getActiveReminder().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(id: Long): ReminderModel? {
        return reminderDao.getReminderById(id)?.toDomain()
    }

    override suspend fun insertTask(task: ReminderModel): Long {
        return reminderDao.insertReminder(task.toEntity())
    }

    override suspend fun insertTasks(tasks: List<ReminderModel>): List<Long> {
        return reminderDao.insertReminders(tasks.map { it.toEntity() })
    }

    override suspend fun updateTask(task: ReminderModel): Int {
        return reminderDao.updateReminder(task.toEntity())
    }

    override suspend fun deleteTask(task: ReminderModel): Int {
        return reminderDao.deleteReminder(task.toEntity())
    }

    override suspend fun deleteAllTasks(): Int = reminderDao.deleteAll()

    override suspend fun deleteTaskById(id: Long): Int {
        return reminderDao.deleteReminderById(id)
    }

    override suspend fun updateTaskStatus(id: Long, isActive: Boolean): Int {
        return reminderDao.updateReminderStatus(id, isActive)
    }


    override suspend fun updateTaskDateTime(id: Long, newDateTime: String, newHourTime: String?): Int {
        return reminderDao.updateReminderDateTime(id, newDateTime, newHourTime)
    }

    override suspend fun getAllTasksOnce(): List<ReminderModel> {
        return reminderDao.getAllRemindersOnce().map { it.toDomain() }
    }

}