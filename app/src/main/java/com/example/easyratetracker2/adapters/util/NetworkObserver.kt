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

    val errorsDescription: String
    var previousStatus: Int?
    var status: Int

    fun observeStatus(lifecycleOwner: LifecycleOwner, lifecycleObserver: Observer<Int>)
    fun observeStatusOnce(endTrigger: (Int)->Boolean)
    fun addError(e: Throwable)

}