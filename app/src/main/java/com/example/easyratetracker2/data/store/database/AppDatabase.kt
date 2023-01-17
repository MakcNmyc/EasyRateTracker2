package com.example.easyratetracker2.data.store.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.easyratetracker2.data.dao.TrackedRateDao
import com.example.easyratetracker2.data.store.room.TrackedRate

@Database(entities = [TrackedRate::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackedRateDao(): TrackedRateDao

    companion object {
        const val TABLE_NAME_TRACKED_RATES = "tracked_rates"
        const val DATABASE_NAME = "database"

        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java, DATABASE_NAME
        ).build()
    }
}