package com.arysapp.reminder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arysapp.reminder.data.local.dao.ReminderDao
import com.arysapp.reminder.data.local.entity.ReminderEntity

@Database(
    entities = [ReminderEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}
