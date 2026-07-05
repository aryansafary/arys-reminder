package com.arysapp.reminder.data.local.dao
import androidx.room.*
import com.arysapp.reminder.data.local.entity.ReminderEntity
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_CREATED_AT
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_ID
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_IS_ACTIVE
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_DATE_TIME
import com.arysapp.reminder.utils.ConstantsDatabase.COLUMN_REMINDER_HOUR_TIME
import com.arysapp.reminder.utils.ConstantsDatabase.REMINDER_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<ReminderEntity>): List<Long>

    @Update
    suspend fun updateReminder(reminder: ReminderEntity): Int


    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity): Int

    @Query("DELETE FROM $REMINDER_TABLE_NAME")
    suspend fun deleteAll(): Int

    @Query("DELETE FROM $REMINDER_TABLE_NAME WHERE $COLUMN_REMINDER_ID = :id")
    suspend fun deleteReminderById(id: Long): Int

    @Query("SELECT * FROM $REMINDER_TABLE_NAME WHERE $COLUMN_REMINDER_ID = :id")
    suspend fun getReminderById(id: Long): ReminderEntity?

    @Query("SELECT * FROM $REMINDER_TABLE_NAME ORDER BY $COLUMN_REMINDER_CREATED_AT DESC")
    fun getAllReminder(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM $REMINDER_TABLE_NAME WHERE $COLUMN_REMINDER_IS_ACTIVE = 1 ORDER BY $COLUMN_REMINDER_DATE_TIME ASC")
    fun getActiveReminder(): Flow<List<ReminderEntity>>

    @Query("UPDATE $REMINDER_TABLE_NAME SET $COLUMN_REMINDER_IS_ACTIVE = :isActive WHERE $COLUMN_REMINDER_ID = :id")
    suspend fun updateReminderStatus(id: Long, isActive: Boolean): Int

    @Query("UPDATE $REMINDER_TABLE_NAME SET $COLUMN_REMINDER_DATE_TIME = :newDateTime, $COLUMN_REMINDER_HOUR_TIME = :newHourTime WHERE $COLUMN_REMINDER_ID = :id")
    suspend fun updateReminderDateTime(id: Long, newDateTime: String, newHourTime: String?): Int

    @Query("SELECT * FROM $REMINDER_TABLE_NAME ORDER BY $COLUMN_REMINDER_CREATED_AT DESC")
    suspend fun getAllRemindersOnce(): List<ReminderEntity>

}
