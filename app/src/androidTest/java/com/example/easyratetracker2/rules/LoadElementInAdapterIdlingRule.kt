package com.example.easyratetracker2.rules

import androidx.test.espresso.IdlingRegistry
import com.example.easyratetracker2.adapters.util.LoadElementObserver
import com.example.easyratetracker2.espresso.LoadElementInAdapter
import com.example.easyratetracker2.tests.util.ScenarioManager
import com.example.easyratetracker2.ui.TestHiltActivity
import kotlinx.coroutines.CoroutineScope
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
            obs = provider(activity)
        }
    }

    private val resource: LoadElementInAdapter = LoadElementInAdapter {
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