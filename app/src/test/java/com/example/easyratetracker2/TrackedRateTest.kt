package com.example.easyratetracker2

import com.example.easyratetracker2.data.store.room.TrackedRate
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

//default values test
class TrackedRateTest {

    var obj: TrackedRate = TrackedRate()

    @Test
    fun test_default_id() {
        assertThat(obj.id, equalTo(0))
    }

    @Test
    fun test_itExistInStorage() {
        assertThat(obj.itExistInStorage(), `is`(false))
    }
}