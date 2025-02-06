package com.example.easyratetracker2.espresso

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import com.example.easyratetracker2.adapters.util.NetworkObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NetworkObserverResource:IdlingResource {

    private val idlingCallbacks: MutableList<ResourceCallback> = ArrayList()
    private var networkObserver: NetworkObserver? = null
    private var notify = false

    override fun getName(): String {
        return "NetworkObserverResource"
    }

    override fun isIdleNow(): Boolean {
        val networkObserverLocal = networkObserver ?: return true

        val idle = isIdleNow(networkObserverLocal.status.value.currentStatus)
        if (idle) {
            whenIdle()
        } else {
            notify = true

            Job().let { j ->
                CoroutineScope(j).launch {
                    networkObserverLocal.status.collectLatest { value ->
                        if (isIdleNow(value.currentStatus)){
                            whenIdle()
                            j.cancel()
                        }
                    }
                }
            }
        }
        return idle
    }

    private fun isIdleNow(status: Int): Boolean {
        return status != NetworkObserver.Status.LOADING
    }

    private fun whenIdle() {
        if (notify) {
            idlingCallbacks.forEach{ obj: ResourceCallback -> obj.onTransitionToIdle() }
        }
        notify = false
    }

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        idlingCallbacks.add(callback)
    }

    fun startObserve(networkObserver: NetworkObserver?) {
        this.networkObserver = networkObserver
    }
}