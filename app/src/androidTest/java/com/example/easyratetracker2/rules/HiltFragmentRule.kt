package com.example.easyratetracker2.rules

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import com.example.easyratetracker2.ui.TestHiltActivity
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.*

//    Hilt does not currently support FragmentScenario
class HiltFragmentRule(
    private val scenarioSupplier: () -> ActivityScenario<TestHiltActivity>,
    private val firstFragment: Class<out Fragment>,
    private val firstFragmentArgs: Bundle? = null
) : TestRule {

    private var firstFragmentTag: String? = null

    fun addFragment(
        fragmentClass: Class<out Fragment>,
        fragmentArgs: Bundle? = null
    ): String {
        val fragmentTag = UUID.randomUUID().toString()
        scenarioSupplier().onActivity { activity: TestHiltActivity ->
            activity.supportFragmentManager.fragmentFactory
                .instantiate(
                    Objects.requireNonNull(fragmentClass.classLoader),
                    fragmentClass.name)
                .also { fragment ->
                    fragmentArgs?.let {
                        fragment.arguments = fragmentArgs
                    }

                    activity.supportFragmentManager
                        .beginTransaction()
                        .add(R.id.content, fragment, fragmentTag)
                        .commitNow()
                }
        }
        return fragmentTag
    }

    fun findFragment(activity: TestHiltActivity, tag: String): Fragment? {
        return activity.supportFragmentManager.findFragmentByTag(tag)
    }

    fun findFirstFragment(activity: TestHiltActivity): Fragment {
        return findFragment(activity, firstFragmentTag!!)!!
    }


    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                firstFragmentTag = addFragment(firstFragment, firstFragmentArgs)
                base.evaluate()
            }
        }
    }
}