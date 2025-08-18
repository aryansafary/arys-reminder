package com.arysapp.task.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arysapp.task.data.local.dao.TaskDao
import com.arysapp.task.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
