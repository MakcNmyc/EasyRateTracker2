package com.example.easyratetracker2.tests

import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.easyratetracker2.R
import com.example.easyratetracker2.RecyclerViewMatchers.atPosition
import com.example.easyratetracker2.adapters.TrackedRatesTestAdapter
import com.example.easyratetracker2.data.models.TrackedIdModel
import com.example.easyratetracker2.data.repositories.TrackedRateRepository
import com.example.easyratetracker2.data.sources.executors.ServiceSourceExecutor
import com.example.easyratetracker2.rules.*
import com.example.easyratetracker2.ui.TestHiltActivity
import com.example.easyratetracker2.ui.lists.TrackedRatesFragment
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.UncheckedIOException
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors
import javax.inject.Inject

@HiltAndroidTest
class TrackedRateTest {

    @get:Rule(order = 0)
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val hiltInjectRule: HiltInjectRule = HiltInjectRule(hiltRule)

    @get:Rule(order = 3)
    val beforeActivityCreatedRule = BeforeActivityCreatedRule (this::beforeActivityCreated)

    @get:Rule(order = 4)
    val activityRule = ActivityScenarioRule(TestHiltActivity::class.java)

    @get:Rule(order = 5)
    val hiltFragmentRule = HiltFragmentRule(
        { activityRule.scenario },
        TrackedRatesFragment::class.java
    )

    @get:Rule(order = 6)
    val scenarioManagerRule = ScenarioManagerRule(activityRule)

    @get:Rule(order = 7)
    val dataBindingIdlingRule = DataBindingIdlingRule(CoroutineScope(Dispatchers.Default), scenarioManagerRule.scenarioManager)

    @get:Rule(order = 8)
    val networkObserverIdlingRule =
        NetworkObserverIdlingRule(CoroutineScope(Dispatchers.Default), scenarioManagerRule.scenarioManager)
        { activity -> getNetworkObserver(hiltFragmentRule, activity) }

    @Inject lateinit var mockWebServer: MockWebServer
    @get:Rule(order = 9)
    val mockWebServerRule: MockWebServerRule = MockWebServerRule { mockWebServer }

    @get:Rule(order = 10)
    val stateDisplayIdlingRule = StateDisplayListIdlingRule(
        CoroutineScope(Dispatchers.Default),
        scenarioManagerRule.scenarioManager,
        { activity -> getLoadElementObs(hiltFragmentRule, activity) },
        { activity -> getNetworkObserver(hiltFragmentRule, activity) }
    )

    @BindValue
    @Mock
    lateinit var trackedRateRepository: TrackedRateRepository

    private fun beforeActivityCreated() {


        val testDbItem = TrackedIdModel(
            TEST_ITEM_ID,
            ServiceSourceExecutor.CBRF_SERVICE,
            TEST_ITEM_OUTER_ID
        )

        Mockito.`when`(
                trackedRateRepository.getTrackedIds(anyInt(), anyInt()))
            .thenReturn(listOf(testDbItem))

        mockWebServer.enqueue(
            MockResponse().setBody(cbrfTestResponse))
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun trackedElementCbrfTest(): Unit = runBlocking {
        Espresso.onView(withId(R.id.list))
            .check(ViewAssertions.matches(atPosition(0, ViewMatchers.withText(RESPONSE_HEADLINE))))
    }


    companion object {
        private const val CBRF_TRACKED_RESPONSE_FILENAME = "cbrf_tracked_test_response.xml"
        private const val TAG = "MainActivityTest"
        const val RESPONSE_HEADLINE = "Test valute name"
        const val TEST_ITEM_OUTER_ID = "9999"
        const val TEST_ITEM_ID = 100L
        val cbrfTestResponse: String
            get() = try {
                BufferedReader(
                    InputStreamReader(
                        InstrumentationRegistry.getInstrumentation().context.assets.open(
                            CBRF_TRACKED_RESPONSE_FILENAME
                        ),
                        StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"))
            } catch (e: IOException) {
                Log.e(TAG, "IOException in getCbrfTrackedResponse()")
                throw UncheckedIOException(e)
            }

        fun getNetworkObserver(
            hiltFragmentRule: HiltFragmentRule,
            activity: TestHiltActivity
        ) = (hiltFragmentRule.findFirstFragment(activity) as TrackedRatesFragment).viewModel.networkObserver

        fun getLoadElementObs(
            hiltFragmentRule: HiltFragmentRule,
            activity: TestHiltActivity
        ) = ((hiltFragmentRule.findFirstFragment(activity) as TrackedRatesFragment).adapter as TrackedRatesTestAdapter).obs

    }
}