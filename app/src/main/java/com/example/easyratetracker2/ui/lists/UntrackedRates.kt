package com.example.easyratetracker2.ui.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.easyratetracker2.adapters.UntrackedRatesAdapter
import com.example.easyratetracker2.databinding.UntrackedRatesBinding
import com.example.easyratetracker2.ui.MainActivity
import com.example.easyratetracker2.ui.createBinding
import com.example.easyratetracker2.ui.setUpStateDisplayList
import com.example.easyratetracker2.viewmodels.lists.UntrackedRatesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UntrackedRates : Fragment() {

    @Inject lateinit var adapter: UntrackedRatesAdapter
    val viewModel: UntrackedRatesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = createBinding(inflater, container, UntrackedRatesBinding::inflate)

        viewModel.init()

        setUpStateDisplayList(
            binding.stateDisplayList,
            viewModel.untrackedRateList,
            adapter,
            viewModel.networkObserver,
            binding.stateDisplayList.swipeRefresh
        ) { viewModel.refreshRateList() }

        adapter.navController = MainActivity.getNavController(this)

        binding.viewModel = viewModel

        binding.sourceDescription.setOnClickListener { v ->
            Navigation.findNavController(v).navigate(
                UntrackedRatesDirections.actionUntrackedRatesListToSourceSelectionList()
            )
        }

        return binding.root
    }
}