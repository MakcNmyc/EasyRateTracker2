package com.example.easyratetracker2.data.store.room

import com.example.easyratetracker2.data.repositories.RoomRepository
import com.example.easyratetracker2.data.store.Storable

interface RoomDataObj : Storable {

    var id: Long

    override fun itExistInStorage(): Boolean {
        return !RoomRepository.itNewObjId(id)
    }
}