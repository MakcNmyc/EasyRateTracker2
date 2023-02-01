package com.example.easyratetracker2.rules

import android.util.Log
import androidx.test.espresso.IdlingRegistry
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.espresso.NetworkObserverResource
import com.example.easyratetracker2.tests.util.ScenarioManager
import com.example.easyratetracker2.ui.TestHiltActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.function.Function

class NetworkObserverIdlingRule(
    scope: CoroutineScope,
    scenarioManager: ScenarioManager<TestHiltActivity>,
    provider: (TestHiltActivity) -> NetworkObserver
) : TestRule {

    private val networkObserverResource: NetworkObserverResource = NetworkObserverResource()

    init {
        scenarioManager.subscribeToActivity(scope) { activity ->
            Log.e("debugShmi", "networkObserverResource startObserve")
            startObserve(provider(activity))
        }
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                IdlingRegistry.getInstance().register(networkObserverResource)
                base.evaluate()
                IdlingRegistry.getInstance().unregister(networkObserverResource)
            }
        }
    }

    fun startObserve(networkObserver: NetworkObserver?) {
        networkObserverResource.startObserve(networkObserver)
    }
}