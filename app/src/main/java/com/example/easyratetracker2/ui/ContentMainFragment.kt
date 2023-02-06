package com.example.easyratetracker2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.easyratetracker2.R
import com.example.easyratetracker2.databinding.ContentMainBinding
import com.example.easyratetracker2.ui.ContentMainFragment.ContentMainPagerAdapter.Companion.SOURCES_NAVIGATION
import com.example.easyratetracker2.ui.ContentMainFragment.ContentMainPagerAdapter.Companion.TRACKED_RATE
import com.example.easyratetracker2.ui.lists.TrackedRates
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ContentMainFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return createBinding(inflater, container, ContentMainBinding::inflate)
            .apply {
                (activity as AppCompatActivity).setSupportActionBar(toolbar)
                viewPager.adapter = ContentMainPagerAdapter(this@ContentMainFragment)
                TabLayoutMediator(
                    tabLayout,
                    viewPager
                ) { tab: TabLayout.Tab, pos: Int ->
                    when(pos){
                        TRACKED_RATE -> tab.text = getString(R.string.main_tab_headline_tracked)
                        SOURCES_NAVIGATION -> tab.text = getString(R.string.main_tab_headline_untracked)
                        else -> throw IndexOutOfBoundsException()
                    }
                }.attach()
            }
            .root
    }

    class ContentMainPagerAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {

        override fun createFragment(position: Int): Fragment {

            return when(position){
                TRACKED_RATE -> TrackedRates()
                SOURCES_NAVIGATION -> SourcesNavigation()
                else -> throw IndexOutOfBoundsException()
            }
        }

        override fun getItemCount(): Int {
            return ITEM_COUNT
        }

        companion object {
            const val TRACKED_RATE = 0
            const val SOURCES_NAVIGATION = 1
            const val ITEM_COUNT = 2
        }
    }
}