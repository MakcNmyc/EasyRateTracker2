package com.example.easyratetracker2.ui.lists

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.easyratetracker2.adapters.TrackedRatesAdapter
import com.example.easyratetracker2.databinding.StateDisplayListBinding
import com.example.easyratetracker2.ui.createAppEntryPoint
import com.example.easyratetracker2.ui.createBinding
import com.example.easyratetracker2.ui.setUpStateDisplayList
import com.example.easyratetracker2.viewmodels.lists.TrackedRatesViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class TrackedRatesFragment : Fragment() {

    @Inject @ApplicationContext lateinit var appContext: Context
    var adapter: TrackedRatesAdapter? = null
    val viewModel: TrackedRatesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createBinding(inflater, container, StateDisplayListBinding::inflate)
            .also { binding ->

                adapter = appContext.createAppEntryPoint().createTrackedRatesAdapter()

                setUpStateDisplayList(
                    binding,
                    viewModel.trackedRateList,
                    adapter!!,
                    viewModel.networkObserver,
                    binding.swipeRefresh
                ) { viewModel.refreshRateList() }

            }.root


    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
}