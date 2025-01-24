package com.example.easyratetracker2.rules

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.IdlingRegistry
import com.example.easyratetracker2.adapters.util.LoadViewHolderObserver
import com.example.easyratetracker2.adapters.util.NetworkObserver
import com.example.easyratetracker2.espresso.StateDisplayListIdlingResource
import com.example.easyratetracker2.tests.util.ScenarioManager
import com.example.easyratetracker2.ui.TestHiltActivity
import kotlinx.coroutines.CoroutineScope
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.lang.ref.WeakReference

class StateDisplayListIdlingRule(
    scope: CoroutineScope,
    scenarioManager: ScenarioManager<TestHiltActivity>,
    loadVhProvider: (TestHiltActivity) -> LoadViewHolderObserver,
    networkObsProvider: (TestHiltActivity) -> NetworkObserver,
) : TestRule {

    private lateinit var loadVhObs: LoadViewHolderObserver
    private lateinit var networkObs: NetworkObserver
    private var _activity: WeakReference<AppCompatActivity?> = WeakReference(null)

    init {
        scenarioManager.subscribeToActivity(scope) { activity ->
            loadVhObs = loadVhProvider(activity)
            networkObs = networkObsProvider(activity)
            _activity = WeakReference(activity)
            Log.e("qwe", "_activity is $activity")
        }
    }

    private val resource = StateDisplayListIdlingResource({_activity.get()}, { loadVhObs }, {networkObs})

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