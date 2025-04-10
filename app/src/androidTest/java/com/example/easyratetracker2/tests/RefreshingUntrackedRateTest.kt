package com.example.easyratetracker2.tests

import android.os.Bundle
import androidx.paging.PagingData
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.easyratetracker2.adapters.UntrackedRatesTestAdapter
import com.example.easyratetracker2.data.models.RatesElementModel
import com.example.easyratetracker2.data.models.UntrackedListModel
import com.example.easyratetracker2.data.sources.executors.ServiceSourceExecutor
import com.example.easyratetracker2.rules.*
import com.example.easyratetracker2.ui.TestHiltActivity
import com.example.easyratetracker2.ui.lists.UntrackedRatesFragment
import com.example.easyratetracker2.viewmodels.lists.UntrackedRatesViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.sameInstance
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@HiltAndroidTest
class RefreshingUntrackedRateTest {

    @Inject
    lateinit var mockWebServer: MockWebServer
    private lateinit var pageListBefore: Flow<Flow<PagingData<RatesElementModel>>?>
    private lateinit var pageListAfter: Flow<Flow<PagingData<RatesElementModel>>?>

    @get:Rule(order = 0)
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val hiltInjectRule: HiltInjectRule = HiltInjectRule(hiltRule)

    @get:Rule(order = 3)
    val beforeActivityCreatedRule: BeforeActivityCreatedRule =
        BeforeActivityCreatedRule (this::beforeActivityCreated)

    @get:Rule(order = 4)
    val activityRule: ActivityScenarioRule<TestHiltActivity> = ActivityScenarioRule(
        TestHiltActivity::class.java
    )

    @get:Rule(order = 5)
    val hiltFragmentRule: HiltFragmentRule = HiltFragmentRule(
        { activityRule.scenario },
        UntrackedRatesFragment::class.java,
        getArgsForUntrackedListItem()
    )

    @get:Rule(order = 6)
    val scenarioManagerRule = ScenarioManagerRule(activityRule)

    @get:Rule(order = 7)
    val dataBindingIdlingRule: DataBindingIdlingRule = DataBindingIdlingRule(
        CoroutineScope(
            Dispatchers.Default), scenarioManagerRule.scenarioManager)

    @get:Rule(order = 8)
    val networkObserverIdlingRule: NetworkObserverIdlingRule =
        NetworkObserverIdlingRule(CoroutineScope(Dispatchers.Default), scenarioManagerRule.scenarioManager)
        { activity -> getNetworkObserver(hiltFragmentRule, activity) }

    @get:Rule(order = 9)
    val mockWebServerRule: MockWebServerRule = MockWebServerRule { mockWebServer }

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

    private fun getArgsForUntrackedListItem() =
        Bundle().apply {
            putParcelable(
                UntrackedRatesViewModel.MODEL_NAME,
                UntrackedListModel("", ServiceSourceExecutor.CBRF_SERVICE)
            )
        }

    @Test
    @Throws(InterruptedException::class)
    fun refreshingUntrackedTest() = runBlocking {
        refreshUntrackedRate(this)
        assertThat(pageListBefore, sameInstance(pageListAfter))
    }

    private suspend fun refreshUntrackedRate(scope: CoroutineScope) {
        suspendCoroutine<Unit> { continuation ->
            activityRule.scenario.onActivity { activity ->
                scope.launch {
                    pageListBefore = getPageList(activity)
                    activityRule.scenario.recreate()
                    activityRule.scenario.onActivity { recreateActivity ->
                        pageListAfter = getPageList(recreateActivity)
                        continuation.resume(Unit)
                    }
                }
            }
        }
    }

    private fun getPageList(activity: TestHiltActivity) = (hiltFragmentRule.findFirstFragment(activity) as UntrackedRatesFragment).viewModel.untrackedRateList

    private fun getNetworkObserver(
        hiltFragmentRule: HiltFragmentRule,
        activity: TestHiltActivity
    ) = (hiltFragmentRule.findFirstFragment(activity) as UntrackedRatesFragment).viewModel.networkObserver

    private fun getLoadElementObs(
        hiltFragmentRule: HiltFragmentRule,
        activity: TestHiltActivity
    ) = (((hiltFragmentRule.findFirstFragment(activity) as UntrackedRatesFragment).adapter)
                as UntrackedRatesTestAdapter).obs

}