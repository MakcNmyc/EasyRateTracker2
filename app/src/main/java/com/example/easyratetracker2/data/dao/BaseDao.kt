package com.example.easyratetracker2.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.easyratetracker2.data.store.room.RoomDataObj

interface BaseDao<T : RoomDataObj> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dbObjList: List<T>): List<Long>

    @Update
    fun update(dbObjList: List<T>): Int

    @Delete
    fun remove(dbObjList: List<T>)
}