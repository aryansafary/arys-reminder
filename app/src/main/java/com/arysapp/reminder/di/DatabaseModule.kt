package com.arysapp.reminder.di

import android.content.Context
import androidx.room.Room
import com.arysapp.reminder.data.local.AppDatabase
import com.arysapp.reminder.data.local.dao.ReminderDao
import com.arysapp.reminder.data.repository.ReminderRepositoryImpl
import com.arysapp.reminder.domain.repository.ReminderRepository
import com.arysapp.reminder.utils.ConstantsDatabase.DATABASE_NAME
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
    fun provideReminderDao(database: AppDatabase): ReminderDao = database.reminderDao()

    @Provides
    @Singleton
    fun provideReminderRepository(reminderDao: ReminderDao): ReminderRepository {
        return ReminderRepositoryImpl(reminderDao)
    }
}