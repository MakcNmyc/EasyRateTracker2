package com.example.easyratetracker2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.easyratetracker2.databinding.SourcesNavigationBinding

class SourcesNavigationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return createBinding(inflater, container, SourcesNavigationBinding::inflate).root
    }
}