package com.example.easyratetracker2.espresso

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.test.espresso.IdlingResource

fun IdlingResource.checkNextFrame(activity: FragmentActivity){
    activity.findViewById<View>(android.R.id.content).postDelayed({
        isIdleNow
    }, 16)
}