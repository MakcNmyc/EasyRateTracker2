package com.example.easyratetracker2.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.easyratetracker2.R
import com.example.easyratetracker2.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater)
            .apply { setContentView(root) }
    }

    companion object {
        // if result null fragment do not support NavController from main activity
        fun getNavController(fragment: Fragment): NavController? {
            val v = fragment.requireActivity()
                .findViewById<View>(R.id.content_main_navigation_container)
            return v?.let { findNavController(it) }
        }
    }
}