package com.example.easyratetracker2.data.sources.executors

import androidx.lifecycle.ViewModel

abstract class PositionalSourceExecutor<T>: ServiceSourceExecutor(){

    abstract fun execute(
        vm: ViewModel,
        startPosition: Int,
        loadSize: Int,
        resultHandler: (result: List<T>) -> Unit,
        errorHandler: (e: Throwable) -> Unit
    )
}