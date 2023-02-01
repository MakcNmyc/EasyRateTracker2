//package com.example.easyratetracker2.rules
//
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.*
//import org.junit.rules.TestRule
//import org.junit.runner.Description
//import org.junit.runners.model.Statement
//
//@ExperimentalCoroutinesApi
//class TestCoroutineRule(
//    val testDispatcher: TestDispatcher = StandardTestDispatcher()
//): TestRule {
//
//    override fun apply(base: Statement, description: Description): Statement {
//        return object : Statement() {
//            @Throws(Throwable::class)
//            override fun evaluate() {
//                Dispatchers.setMain(testDispatcher)
//                base.evaluate()
//                Dispatchers.resetMain()
//            }
//        }
//    }
//
//    fun createTestScope() =
//        CoroutineScope(testDispatcher)
//
//    fun runBlockingTest(block: suspend () -> Unit) = runTest(this.testDispatcher) {
//        block()
//    }
//}