package com.example.easyratetracker2.rules

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class BeforeActivityCreatedRule(var task: () -> Unit) : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                task()
                base.evaluate()
            }
        }
    }
}