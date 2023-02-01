package com.example.easyratetracker2.rules

import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class HiltInjectRule(private val rule: HiltAndroidRule) : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                rule.inject()
                base.evaluate()
            }
        }
    }
}