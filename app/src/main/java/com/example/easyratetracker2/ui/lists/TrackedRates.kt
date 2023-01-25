package com.example.easyratetracker2.ui.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.easyratetracker2.adapters.TrackedRatesAdapter
import com.example.easyratetracker2.adapters.UntrackedRatesAdapter
import com.example.easyratetracker2.databinding.TrackedRatesBinding
import com.example.easyratetracker2.ui.createBinding
import com.example.easyratetracker2.ui.setUpNetworkList
import com.example.easyratetracker2.viewmodels.lists.TrackedRatesViewModel
import com.example.easyratetracker2.viewmodels.lists.UntrackedRatesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TrackedRates : Fragment() {

    @Inject lateinit var adapter: TrackedRatesAdapter
    val viewModel: TrackedRatesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return createBinding(inflater, container, TrackedRatesBinding::inflate)
            .also { binding ->
                setUpNetworkList(
                    binding.trackedRatesList,
                    { viewModel.pagedList },
                    adapter,
                    viewModel.networkObserver,
                    binding.swipeRefresh,
                    {viewModel.refreshPagedList()}
                )
            }.root
    }
}