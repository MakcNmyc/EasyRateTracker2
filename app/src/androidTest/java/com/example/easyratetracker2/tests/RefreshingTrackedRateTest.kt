package com.example.easyratetracker2.tests

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.easyratetracker2.R
import com.example.easyratetracker2.RecyclerViewMatchers.atPosition
import com.example.easyratetracker2.RecyclerViewMatchers.matchSize
import com.example.easyratetracker2.data.repositories.TrackedRateRepository
import com.example.easyratetracker2.data.repositories.utilities.StorageRequest
import com.example.easyratetracker2.data.sources.executors.ServiceSourceExecutor
import com.example.easyratetracker2.data.store.room.TrackedRate
import com.example.easyratetracker2.rules.*
import com.example.easyratetracker2.tests.TrackedRateTest.Companion.RESPONSE_HEADLINE
import com.example.easyratetracker2.tests.TrackedRateTest.Companion.TEST_ITEM_OUTER_ID
import com.example.easyratetracker2.tests.TrackedRateTest.Companion.getLoadElementObs
import com.example.easyratetracker2.tests.TrackedRateTest.Companion.getNetworkObserver
import com.example.easyratetracker2.ui.TestHiltActivity
import com.example.easyratetracker2.ui.lists.TrackedRatesFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import javax.inject.Inject

@HiltAndroidTest
class RefreshingTrackedRateTest {

    @Inject lateinit var mockWebServer: MockWebServer
    @Inject lateinit var repository: TrackedRateRepository

    @get:Rule(order = 0)
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val hiltInjectRule: HiltInjectRule = HiltInjectRule(hiltRule)

    @get:Rule(order = 3)
    val beforeActivityCreatedRule = BeforeActivityCreatedRule (this::beforeActivityCreated)

    @get:Rule(order = 4)
    val activityRule: ActivityScenarioRule<TestHiltActivity> = ActivityScenarioRule(
        TestHiltActivity::class.java
    )

    @get:Rule(order = 5)
    val hiltFragmentRule = HiltFragmentRule(
        { activityRule.scenario },
        TrackedRatesFragment::class.java
    )

    @get:Rule(order = 6)
    val scenarioManagerRule = ScenarioManagerRule(activityRule)

    @get:Rule(order = 7)
    val dataBindingIdlingRule = DataBindingIdlingRule(
        CoroutineScope(
            Dispatchers.Default
        ), scenarioManagerRule.scenarioManager
    )

    @get:Rule(order = 8)
    val networkObserverIdlingRule =
        NetworkObserverIdlingRule(CoroutineScope(Dispatchers.Default), scenarioManagerRule.scenarioManager)
        { activity -> getNetworkObserver(hiltFragmentRule, activity) }

    @get:Rule(order = 9)
    val mockWebServerRule = MockWebServerRule { mockWebServer }

    @get:Rule(order = 10)
    val stateDisplayListIdlingRule = StateDisplayListIdlingRule(
        CoroutineScope(Dispatchers.Default),
        scenarioManagerRule.scenarioManager,
        { activity -> getLoadElementObs(hiltFragmentRule, activity) },
        { activity -> getNetworkObserver(hiltFragmentRule, activity) }
    )


    private fun beforeActivityCreated() {
        mockWebServer.enqueue(
            MockResponse().setBody(TrackedRateTest.cbrfTestResponse)
        )
    }

    @Test
    fun refreshingTrackedRateTest():Unit = runBlocking {
        Espresso.onView(withId(R.id.list)).check(ViewAssertions.matches(matchSize(0)))
        TrackedRate(TEST_ITEM_OUTER_ID, ServiceSourceExecutor.CBRF_SERVICE).also { newItem ->
            repository.saveToDb(StorageRequest(listOf(newItem)))
        }
        scenarioManagerRule.scenarioManager.recreate(activityRule.scenario)
        Espresso.onView(withId(R.id.list))
            .check(ViewAssertions.matches(atPosition(0, withText(RESPONSE_HEADLINE))))
    }
}