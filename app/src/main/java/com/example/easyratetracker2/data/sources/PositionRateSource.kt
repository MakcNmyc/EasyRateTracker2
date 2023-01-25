package com.example.easyratetracker2.data.sources

import androidx.paging.PositionalDataSource
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.data.sources.executors.PositionalSourceExecutor
import kotlinx.coroutines.CoroutineScope

class PositionRateSource<T>(
    private val networkObserver: NetworkObserver,
    private val scope: CoroutineScope,
    private val executor: PositionalSourceExecutor<T>,
) : PositionalDataSource<T>() {

    private var dataStoreEnded = false

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
        startRequest(
            params.requestedStartPosition,
            params.requestedLoadSize,
        ) { result -> callback.onResult(result, params.requestedStartPosition) }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
        startRequest(
            params.startPosition,
            params.loadSize,
            callback::onResult
        )
    }

    private fun startRequest(startPosition: Int,
                             requiredLoadSize: Int,
                             resultNotify: (result: List<T>) -> Unit) {

        if (dataStoreEnded && startPosition != 0) {
            resultHandler(requiredLoadSize, resultNotify).invoke(emptyList())
            return
        }

        networkObserver.status = NetworkObserver.Status.LOADING
        executor.execute(
            scope,
            startPosition,
            requiredLoadSize,
            resultHandler(requiredLoadSize, resultNotify),
            this::onError)
    }

    private fun onError(e: Throwable) {
        networkObserver.addError(e)
    }

    private inline fun resultHandler(
        requiredLoadSize: Int,
        crossinline resultNotify: (result: List<T>) -> Unit,
    ): (result: List<T>) -> Unit =
        { result: List<T> ->
            if (result.size < requiredLoadSize)
                dataStoreEnded = true
            networkObserver.status = NetworkObserver.Status.READY
            resultNotify(result)
        }


//    private inline fun emptyResultHandler(
//        crossinline resultNotify: (result: List<T>) -> Unit
//    ): () -> Unit = {
//        networkObserver.status = NetworkObserver.Status.READY
//        resultNotify(emptyList())
//    }
}