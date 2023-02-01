package com.example.easyratetracker2.rules

import androidx.test.espresso.IdlingRegistry
import com.example.easyratetracker2.espresso.DataBindingIdlingResource
import com.example.easyratetracker2.espresso.monitorActivity
import com.example.easyratetracker2.tests.util.ScenarioManager
import com.example.easyratetracker2.ui.TestHiltActivity
import kotlinx.coroutines.CoroutineScope
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class DataBindingIdlingRule(scope: CoroutineScope, scenarioManager: ScenarioManager<TestHiltActivity>) : TestRule {

    private val dataBindingIdlingResource: DataBindingIdlingResource = DataBindingIdlingResource().also {
        scenarioManager.subscribeToActivity(scope, it::monitorActivity)
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                IdlingRegistry.getInstance().register(dataBindingIdlingResource)
                base.evaluate()
                IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
            }
        }
    }
}