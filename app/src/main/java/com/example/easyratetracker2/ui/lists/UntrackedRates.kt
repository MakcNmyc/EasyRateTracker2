package com.example.easyratetracker2.ui.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UntrackedRates : Fragment() {

    private var binding: UntrackedRatesBinding? = null
    var recyclerView: RecyclerView? = null
        private set

    @Inject
    protected var adapter: UntrackedRatesAdapter? = null
    private var viewModel: UntrackedRatesViewModel? = null
    fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UntrackedRatesBinding.inflate(inflater, container, false)
        adapter.setNavController(MainActivity.getNavController(this))
        recyclerView = binding.untrackedRatesList
        viewModel = ViewModelProvider(this).get(UntrackedRatesViewModel::class.java)
        binding.sourceDescription.setOnClickListener { v ->
            findNavController(v).navigate(
                UntrackedRatesDirections.actionUntrackedRatesListToSourceSelectionList()
            )
        }
        return binding.getRoot()
    }

    protected fun setUpPageList() {
        viewModel.getItem().observe(this) { v -> super.setUpPageList() }
    }

    fun setContent() {
        super.setContent()
        binding.setViewModel(viewModel)
    }

    fun setUpNetworkFunctionality() {
        setUpNetworkFunctionality(binding.swipeRefresh, viewModel.getNetworkObserver(), adapter)
    }

    val recyclerViewAdapter: BaseAdapter<UntrackedRatesElementItem>?
        get() = adapter

    fun getViewModel(): BaseListViewModel<UntrackedRatesElementItem>? {
        return viewModel
    }

    fun getBinding(): ViewDataBinding? {
        return binding
    }
}