package com.arysapp.reminder.domain.repository

import com.arysapp.reminder.domain.model.ReminderModel
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getAllReminders(): Flow<List<ReminderModel>>
    fun getActiveReminders(): Flow<List<ReminderModel>>
    suspend fun getReminderById(id: Long): ReminderModel?
    suspend fun insertReminder(reminder: ReminderModel): Long
    suspend fun insertReminders(reminders: List<ReminderModel>): List<Long>
    suspend fun updateReminder(reminder: ReminderModel): Int
    suspend fun deleteReminder(reminder: ReminderModel): Int
    suspend fun deleteAllReminders(): Int
    suspend fun deleteReminderById(id: Long): Int
    suspend fun updateReminderStatus(id: Long, isActive: Boolean): Int
    suspend fun updateReminderDateTime(id: Long, newDateTime: String, newHourTime: String?): Int
    suspend fun getAllRemindersOnce(): List<ReminderModel>
}