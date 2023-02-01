package com.example.easyratetracker2.data.models

data class ListErrorModel(var description : String) : ListElementModel<Long>{

    override val id: Long = ERROR_ID

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListErrorModel

        if (description != other.description) return false
        if (id != other.id) return false

        return true
    }
    override fun hashCode(): Int {
        var result = description.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }

    companion object {
        private const val ERROR_ID = -1L
    }

}