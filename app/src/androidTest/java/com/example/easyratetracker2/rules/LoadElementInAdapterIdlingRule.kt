package com.example.easyratetracker2.rules

import android.util.Log
import androidx.test.espresso.IdlingRegistry
import com.example.easyratetracker2.adapters.util.LoadElementObserver
import com.example.easyratetracker2.espresso.LoadElementInAdapter
import com.example.easyratetracker2.tests.util.ScenarioManager
import com.example.easyratetracker2.ui.TestHiltActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class LoadElementInAdapterIdlingRule(
    scope: CoroutineScope,
    scenarioManager: ScenarioManager<TestHiltActivity>,
    provider: (TestHiltActivity) -> LoadElementObserver
) : TestRule {

    private var obs: LoadElementObserver? = null

    init {
        scenarioManager.subscribeToActivity(scope) { activity ->
            Log.e("debugShmi", "LoadElementInAdapterIdlingRule obs init")
            obs = provider(activity)
        }
    }

    private val resource: LoadElementInAdapter = LoadElementInAdapter {
        Log.e("debugShmi", "LoadElementInAdapterIdlingRule obs return is $obs")
        obs!! }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                IdlingRegistry.getInstance().register(resource)
                base.evaluate()
                IdlingRegistry.getInstance().unregister(resource)
            }
        }
    }
}