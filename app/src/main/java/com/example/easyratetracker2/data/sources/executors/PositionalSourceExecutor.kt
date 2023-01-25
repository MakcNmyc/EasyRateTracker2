package com.example.easyratetracker2.data.sources.executors

import kotlinx.coroutines.CoroutineScope

abstract class PositionalSourceExecutor<T>: ServiceSourceExecutor(){

    abstract fun execute(
        scope: CoroutineScope,
        startPosition: Int,
        loadSize: Int,
        resultHandler: (result: List<T>) -> Unit,
        errorHandler: (e: Throwable) -> Unit
    )
}