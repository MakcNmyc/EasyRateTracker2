package com.example.easyratetracker2.data.sources

import androidx.paging.PositionalDataSource
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.sources.executors.PositionalSourceExecutor

class PositionRateSource<T>(
    private val networkObserver: NetworkObserver,
    val executor: PositionalSourceExecutor<T>,
) : PositionalDataSource<T>() {

    private var dataStoreEnded = false

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
        startRequest(
            params.requestedStartPosition,
            params.requestedLoadSize,
            resultHandler { result -> callback.onResult(result, params.requestedStartPosition) },
            )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
        startRequest(
            params.startPosition,
            params.loadSize,
            resultHandler(callback::onResult)
        )
    }

    fun startRequest(startPosition: Int,
                     requiredLoadSize: Int,
                     resultHandler: (result: List<T>, requiredLoadSize: Int) -> Unit) {

        if (dataStoreEnded && startPosition != 0) {
            pushEmptyResult(resultHandler)
            return
        }

        networkObserver.status = NetworkObserver.Status.LOADING
        executor.execute(startPosition, requiredLoadSize, resultHandler, this::onError) {
            pushEmptyResult(resultHandler)
        }
    }

    fun onError(e: Throwable) {
        networkObserver.addError(e)
    }

    private inline fun resultHandler(
        crossinline resultNotify: (result: List<T>) -> Unit
    ): (result: List<T>, requiredLoadSize: Int) -> Unit =
        { result: List<T>, requiredLoadSize: Int ->
            if (result.size < requiredLoadSize || requiredLoadSize == 0)
                dataStoreEnded = true
            resultNotify(result)
            networkObserver.status = NetworkObserver.Status.READY
        }


    private inline fun pushEmptyResult(resultHandler: (result: List<T>, requiredLoadSize: Int) -> Unit) {
        networkObserver.status = NetworkObserver.Status.READY
        resultHandler(emptyList(), 0)
    }
}