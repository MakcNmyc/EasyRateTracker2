package com.example.easyratetracker2.adapters.util

import kotlinx.coroutines.flow.StateFlow

interface NetworkObserver {

    object Status {
        const val INIT = 0
        const val READY = 1
        const val LOADING = 2
        const val ERROR = 3
    }

    data class StatusData(
        val currentStatus: Int,
        val previousStatus: Int?,
    ){
        var error: Throwable? = null

        fun createFrom(newStatus: Int) =
            if(currentStatus == newStatus) this else StatusData(newStatus, currentStatus)

        fun createAndAddError(e: Throwable) =
            (if (currentStatus != Status.ERROR) StatusData(Status.ERROR, currentStatus)
            else copy()).also {
                it.error = e
            }
    }

    val errorsDescription: String
    val status: StateFlow<StatusData>

    fun setStatus(status: Int)
    fun addError(e: Throwable)

}