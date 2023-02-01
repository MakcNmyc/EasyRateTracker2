package com.example.easyratetracker2

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.util.*
import java.util.stream.IntStream

object RecyclerViewMatchers {

    fun matchHolder(itemMatcher: Matcher<View?>): Matcher<RecyclerView.ViewHolder> {
        return object : BoundedMatcher<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(
            RecyclerView.ViewHolder::class.java
        ) {
            override fun matchesSafely(item: RecyclerView.ViewHolder): Boolean =
                itemMatcher.matches(item.itemView)


            override fun describeTo(description: Description) {
                description.appendText("view holder with matcher: $itemMatcher")
            }
        }
    }

    fun matchSize(size: Int): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun matchesSafely(rw: RecyclerView): Boolean =
                size == rw.adapter!!.itemCount


            override fun describeTo(description: Description) {
                description.appendText("with list size: $size")
            }
        }
    }

    fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean =
                view.findViewHolderForLayoutPosition(position)?.let {
                    matchAllChildren(it.itemView, itemMatcher)
                } ?: false
        }
    }

    private fun matchAllChildren(view: View, matcher: Matcher<View>): Boolean {
        if (view !is ViewGroup) return matcher.matches(view)

        return runBlocking{
            (0..view.childCount)
                .asFlow()
                .map { view.getChildAt(it) }
                .filter { it != null }
                .map { matchAllChildren(it, matcher) }
                .flowOn(Dispatchers.Default)
                .fold(false){ sum, value -> sum || value }
        }
    }
}