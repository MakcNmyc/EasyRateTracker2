package com.example.easyratetracker2.data.store.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.easyratetracker2.data.repositories.RoomRepository
import com.example.easyratetracker2.data.store.database.AppDatabase

@Entity(tableName = AppDatabase.TABLE_NAME_SELECTABLE_SOURCES)
data class SelectableSource(
    var name: String,
    var description: String,
    var currency: String,
    var language: String,
    @ColumnInfo(name = "receiving_method") var receivingMethod: Int,
    @PrimaryKey(autoGenerate = true) override var id: Long = RoomRepository.DEFAULT_ID
) : RoomDataObj