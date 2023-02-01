package com.example.easyratetracker2.adapters

import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.data.models.SourceSelectionModel
import com.example.easyratetracker2.data.models.UntrackedListModel
import com.example.easyratetracker2.databinding.SourceSelectionElementBinding
import com.example.easyratetracker2.ui.lists.SourceSelectionListDirections
import javax.inject.Inject

class SourceSelectionAdapter @Inject constructor(itemCallback: ItemCallback<SourceSelectionModel>) :
    ModelAdapter<SourceSelectionModel>(itemCallback) {

    override val vhProducer: (parent: ViewGroup) -> ModelViewHolder<SourceSelectionModel, SourceSelectionElementBinding> =
        { parent ->
            ModelViewHolder(
                parent,
                SourceSelectionElementBinding::inflate
            ) { model, binding ->
                binding.model = model
                binding.root.setOnClickListener { view ->
                    Navigation.findNavController(view).navigate(
                        SourceSelectionListDirections.actionSourceSelectionListToUntrackedRatesList(
                            UntrackedListModel(model)
                        )
                    )
                }
            }
        }
}