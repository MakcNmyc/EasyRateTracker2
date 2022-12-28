package com.example.easyratetracker2.data.sources.executors

abstract class PositionalSourceExecutor<T>: ServiceSourceExecutor(){

    abstract fun execute(startPosition: Int,
                         loadSize: Int,
                         resultHandler: (result: List<T>, loadSize: Int) -> Unit,
                         errorHandler: (e: Throwable) -> Unit,
                         emptyResultHandler: () -> Unit)
}