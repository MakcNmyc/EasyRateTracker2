package com.example.easyratetracker2.rules

import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.function.Supplier

class MockWebServerRule(private val serverSupplier: Supplier<MockWebServer>)
    : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                base.evaluate()
                serverSupplier.get().shutdown()
            }
        }
    }
}