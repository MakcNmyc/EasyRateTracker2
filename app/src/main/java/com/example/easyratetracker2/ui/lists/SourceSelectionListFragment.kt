package com.example.easyratetracker2.ui.lists

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.easyratetracker2.databinding.SourceSelectionListBinding
import com.example.easyratetracker2.ui.createAppEntryPoint
import com.example.easyratetracker2.ui.createBinding
import com.example.easyratetracker2.ui.setUpBaseList
import com.example.easyratetracker2.viewmodels.lists.SourceSelectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class SourceSelectionListFragment : Fragment() {

    @Inject
    @ApplicationContext
    lateinit var appContext: Context
    
    val viewModel: SourceSelectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        createBinding(inflater, container, SourceSelectionListBinding::inflate).also { binding ->
            // TODO: change to state observer then user can see load animation when sources init or take init in first launch app
            setUpBaseList(
                binding.sourceSelectionList,
                viewModel.sourceList,
                appContext.createAppEntryPoint().createSourceSelectionAdapter()
            )
        }.root
}