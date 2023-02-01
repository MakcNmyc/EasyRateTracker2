package com.example.easyratetracker2.tests.util

import android.app.Activity
import android.util.Log
import androidx.test.core.app.ActivityScenario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ScenarioManager<T: Activity>() {

    lateinit var scenario: ActivityScenario<T>

    private val _scenarioObserver: MutableStateFlow<T?> = MutableStateFlow(null)
    val scenarioObserver: StateFlow<T?> = _scenarioObserver

//    fun recreate(scenarioProducer: ()-> ActivityScenario<T>) {
//        scenarioObserver.value!!.recreate()
//        notifySubs(scenarioProducer())
//    }

//    fun notifySubs(newValue: ActivityScenario<T>) {
//        scenarioObserver.value = newValue
//    }

    fun initScenario(scenario: ActivityScenario<T>){
        Log.e("debugShmi", "ScenarioManager initScenario")
        this.scenario = scenario
        this.scenario.onActivity{
            Log.e("debugShmi", "ScenarioManager onActivity")
            _scenarioObserver.value = it
        }
    }

    inline fun subscribeToActivity(scope: CoroutineScope, crossinline sub: (T)->Unit){
        scope.launch  {

            scenarioObserver.collect{v ->
                Log.e("debugShmi", "ScenarioManager subscribeToActivity $v")
                v?.let { sub(v) }
            }
        }
    }
}