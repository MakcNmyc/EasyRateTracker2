package com.example.easyratetracker2.data.repositories.utilities

import com.example.easyratetracker2.data.store.Storable

class StorageRequest<T: Storable> (var objects: List<T>) {

    private val errorsCodes: MutableList<Int> by lazy{ArrayList()}
    var isDeny = false

    fun addError(e: Int) {
        isDeny = true
        errorsCodes.add(e)
    }

    companion object{
        const val INSERT_ERROR: Int = 0
        const val UPDATE_ERROR: Int = 1
    }
}