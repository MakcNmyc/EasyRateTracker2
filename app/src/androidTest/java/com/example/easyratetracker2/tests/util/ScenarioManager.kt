package com.example.easyratetracker2.tests.util

import android.app.Activity
import android.util.Log
import androidx.test.core.app.ActivityScenario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScenarioManager<T: Activity>() {

    lateinit var scenario: ActivityScenario<T>

    private val _activityObserver: MutableStateFlow<T?> = MutableStateFlow(null)
    val activityObserver: StateFlow<T?> = _activityObserver.asStateFlow()

    fun recreate(scenario: ActivityScenario<T>) {
        scenario.recreate()
        notifySubs(scenario)
    }

    fun notifySubs(scenario: ActivityScenario<T>) {
        scenario.onActivity{ newActivity ->
            _activityObserver.value = newActivity
        }
    }

    fun initScenario(scenario: ActivityScenario<T>){
        Log.e("debugShmi", "ScenarioManager initScenario")
        this.scenario = scenario
        this.scenario.onActivity{
            Log.e("debugShmi", "ScenarioManager onActivity")
            _activityObserver.value = it
        }
    }

    inline fun subscribeToActivity(scope: CoroutineScope, crossinline sub: (T)->Unit){
        scope.launch  {
            activityObserver.collect{ v ->
                Log.e("debugShmi", "ScenarioManager subscribeToActivity $v")
                v?.let { sub(v) }
            }
        }
    }
}