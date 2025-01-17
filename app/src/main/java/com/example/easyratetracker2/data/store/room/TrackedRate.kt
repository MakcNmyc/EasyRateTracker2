package com.example.easyratetracker2.data.store.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.easyratetracker2.data.repositories.RoomRepository
import com.example.easyratetracker2.data.repositories.RoomRepository.Companion.INT_STUB
import com.example.easyratetracker2.data.repositories.RoomRepository.Companion.STRING_STUB

import com.example.easyratetracker2.data.store.database.AppDatabase

@Entity(tableName = AppDatabase.TABLE_NAME_TRACKED_RATES)
class TrackedRate(
    val outerId: String = STRING_STUB,
    val sourceId: Int = INT_STUB,
    @PrimaryKey(autoGenerate = true) override var id: Long = RoomRepository.DEFAULT_ID
): RoomDataObj