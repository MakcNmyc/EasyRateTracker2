package com.example.easyratetracker2.adapters.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface NetworkObserver {

    object Status {
        const val INIT = 0
        const val READY = 1
        const val LOADING = 2
        const val ERROR = 3
    }


    data class StatusData(
        val newStatus: Int,
        val previousStatus: Int?
    )

    val errorsDescription: String
    var status: Int

    fun observeStatusData(lifecycleOwner: LifecycleOwner, lifecycleObserver: Observer<StatusData>)
    fun observeStatusBeforeTriggered(endTrigger: (StatusData)->Boolean)
    fun addError(e: Throwable)

}