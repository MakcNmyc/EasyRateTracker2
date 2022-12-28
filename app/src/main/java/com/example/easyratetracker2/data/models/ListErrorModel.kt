package com.example.easyratetracker2.data.models

data class ListErrorModel(var description : String) : Model<Long>{

    override var id: Long
        get() = ERROR_ID
        set(value) {}

    override fun equals(other: Any?): Boolean = false

    companion object {
        private const val ERROR_ID = -1L;
    }

}