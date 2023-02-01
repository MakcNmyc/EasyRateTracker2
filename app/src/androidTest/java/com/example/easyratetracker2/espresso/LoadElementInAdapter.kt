package com.example.easyratetracker2.espresso

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import com.example.easyratetracker2.adapters.util.LoadElementObserver

class LoadElementInAdapter(private val observerProducer: ()-> LoadElementObserver): IdlingResource {

    private val idlingCallbacks: MutableList<ResourceCallback> = ArrayList()

    override fun getName(): String {
        return "LoadElementInAdapter"
    }

    override fun isIdleNow(): Boolean {
        val idle: Boolean = observerProducer().notHaveLoadElement()
        if (idle) {
            whenIdle()
        } else {
            observerProducer().subscribe { whenIdle() }
        }
        return idle
    }

    private fun whenIdle() {
        idlingCallbacks.forEach { obj: ResourceCallback -> obj.onTransitionToIdle() }
    }

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        idlingCallbacks.add(callback)
    }
}