package com.arysapp.task.di

import android.content.Context
import androidx.room.Room
import com.arysapp.task.data.local.AppDatabase
import com.arysapp.task.data.local.dao.TaskDao
import com.arysapp.task.data.repository.TaskRepositoryImpl
import com.arysapp.task.domain.repository.TaskRepository
import com.arysapp.task.utils.ConstantsDatabase.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration(false).build()


    @Provides
    @Singleton
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(taskDao)
    }
}