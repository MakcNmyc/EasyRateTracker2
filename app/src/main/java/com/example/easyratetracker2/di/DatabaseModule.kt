package com.example.easyratetracker2.di

import android.content.Context
import com.example.easyratetracker2.data.dao.SelectableSourceDao
import com.example.easyratetracker2.data.dao.TrackedRateDao
import com.example.easyratetracker2.data.store.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideTrackedReteDao(database: AppDatabase): TrackedRateDao {
        return database.trackedRateDao()
    }

    @Provides
    fun provideSelectableSourceDao(database: AppDatabase): SelectableSourceDao {
        return database.selectableSourceDao()
    }
}