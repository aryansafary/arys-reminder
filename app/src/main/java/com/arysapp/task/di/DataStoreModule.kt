package com.arysapp.task.di

import android.content.Context
import com.arysapp.task.data.datastore.DataStoreImpl
import com.arysapp.task.data.datastore.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext context: Context):
        DataStoreRepository = DataStoreImpl(context)
}