package com.example.easyratetracker2.di

import android.content.Context
import androidx.room.Room
import com.example.easyratetracker2.data.dao.SelectableSourceDao
import com.example.easyratetracker2.data.dao.TrackedRateDao
import com.example.easyratetracker2.data.store.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DatabaseModule::class])
class DatabaseTestModule {

    @Singleton
    @Provides
    fun createAppDatabase(@ApplicationContext context: Context?): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context!!, AppDatabase::class.java).build()
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