package com.example.easyratetracker2.data.sources.executors

import android.content.Context
import androidx.collection.ArrayMap
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

abstract class ServiceSourceExecutor{

    @ApplicationContext
    @Inject
    lateinit var context: Context
    internal val supportedServices: MutableMap<Int, Any> = ArrayMap()

    @Suppress("UNCHECKED_CAST")
    internal inline fun <V : Any> getService(servicesId: Int, producer: () -> V): V {
        var result = supportedServices[servicesId]
        if (result == null) {
            result = producer()
            if (supportedServices.size >= MAX_SERVICE_AMOUNT) supportedServices.clear()
            supportedServices[servicesId] = result
        }
        return result as V
    }

    companion object {
        internal const val MAX_SERVICE_AMOUNT = 5

        //All supported services IDs
        const val CBRF_SERVICE = 1
        val allSupportedServices: HashSet<Int> = HashSet<Int>().apply {
            add(CBRF_SERVICE)
        }
    }
}