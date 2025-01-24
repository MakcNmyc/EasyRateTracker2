package com.example.easyratetracker2.espresso

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import com.example.easyratetracker2.R
import com.example.easyratetracker2.adapters.util.LoadViewHolderObserver
import com.example.easyratetracker2.adapters.util.NetworkObserver

class StateDisplayListIdlingResource(
    private val activityProvider: (() -> AppCompatActivity?),
    private val loadVhObsProvider: () -> LoadViewHolderObserver,
    private val networkObsProvider: () -> NetworkObserver,
) : IdlingResource {

    private var wasIdle = true

    private val idlingCallbacks: MutableList<ResourceCallback> = ArrayList()

    override fun getName(): String {
        return "LoadElementInAdapter"
    }

    override fun isIdleNow(): Boolean {

        val networkStatus = networkObsProvider().status
        val isIdleNow: Boolean
        val loadElementInActivity: Boolean
        var loadElementInList = false
        val activity = activityProvider()

        when(networkStatus){
            NetworkObserver.Status.LOADING, NetworkObserver.Status.INIT -> isIdleNow = false
            NetworkObserver.Status.ERROR -> isIdleNow = true
            else -> {
                loadElementInList = loadVhObsProvider().haveActiveLoadElements()
                loadElementInActivity = !loadElementInList && activity?.findViewById<View>(R.id.loadingBar)?.visibility == View.VISIBLE
                isIdleNow = !(loadElementInList || loadElementInActivity)
            }
        }

        if (isIdleNow) {
            if(!wasIdle) whenIdle()
        } else {
            if(loadElementInList){
                if(!wasIdle) loadVhObsProvider().onElementDisappear { whenIdle() }
            }else{
                if(activity != null) checkNextFrame(activity)
            }
        }

        wasIdle = isIdleNow

        return isIdleNow
    }

    private fun whenIdle() {
        idlingCallbacks.forEach { it.onTransitionToIdle() }
    }

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        idlingCallbacks.add(callback)
    }
}