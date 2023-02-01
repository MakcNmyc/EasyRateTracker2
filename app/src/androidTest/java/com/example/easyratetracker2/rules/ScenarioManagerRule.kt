package com.example.easyratetracker2.rules

import androidx.fragment.app.FragmentActivity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.easyratetracker2.tests.util.ScenarioManager
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ScenarioManagerRule<T:FragmentActivity>(val activityRule: ActivityScenarioRule<T>) : TestRule {

    val scenarioManager = ScenarioManager<T>()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                scenarioManager.initScenario(activityRule.scenario)
                base.evaluate()
            }
        }
    }
}