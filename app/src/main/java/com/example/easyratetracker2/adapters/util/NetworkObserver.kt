package com.example.easyratetracker2.adapters.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import io.reactivex.disposables.CompositeDisposable
import java.util.function.Function

interface NetworkObserver {
    interface Factory {
        fun create(disposables: CompositeDisposable?): NetworkObserver?
    }

    object Status {
        const val INIT = 0
        const val READY = 1
        const val LOADING = 2
        const val ERROR = 3
    }

    val errorsDescription: String
    val disposables: CompositeDisposable?
    var previousStatus: Int?
    var status: Int

    fun observeStatus(lifecycleOwner: LifecycleOwner, lifecycleObserver: Observer<Int>)
    fun observeStatusOnce(endTrigger: (Int)->Boolean)
    fun addError(e: Throwable)

}