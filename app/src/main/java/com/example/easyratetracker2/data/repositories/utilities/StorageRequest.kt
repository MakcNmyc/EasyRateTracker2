package com.example.easyratetracker2.data.repositories.utilities

import com.example.easyratetracker2.data.store.Storable

class StorageRequest<T: Storable> (var objects: List<T>) {

    val errorsCodes: MutableList<Int> by lazy{ArrayList()}
    var isDeny = false

    fun addError(e: Int) {
        isDeny = true
        errorsCodes.add(e)
    }

    companion object{
        val INSERT_ERROR: Int = 0
        val UPDATE_ERROR: Int = 1
    }
}