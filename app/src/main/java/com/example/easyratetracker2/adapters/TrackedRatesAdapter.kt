package com.example.easyratetracker2.adapters

import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.data.models.OuterDetailsModel
import com.example.easyratetracker2.data.models.TrackedRatesElementModel
import com.example.easyratetracker2.databinding.TrackedRatesElementBinding
import com.example.easyratetracker2.ui.ContentMainFragmentDirections
import javax.inject.Inject

class TrackedRatesAdapter @Inject constructor(itemCallback: ItemCallback<TrackedRatesElementModel>) :
    StateDisplayAdapter<TrackedRatesElementModel>(itemCallback) {

    override val vhProducer: (parent: ViewGroup) -> ModelViewHolder<TrackedRatesElementModel, TrackedRatesElementBinding> =
        { parent ->
            ModelViewHolder(
                parent,
                TrackedRatesElementBinding::inflate,
                this::setUpData
            )
        }

    private fun setUpData(
        model: TrackedRatesElementModel,
        binding: TrackedRatesElementBinding
    ) {
        binding.model = model
        binding.root.setOnClickListener{ v ->
            Navigation.findNavController(v).navigate(
                ContentMainFragmentDirections.actionContentMainToRateDetails(OuterDetailsModel(model))
            )
        }
    }

}