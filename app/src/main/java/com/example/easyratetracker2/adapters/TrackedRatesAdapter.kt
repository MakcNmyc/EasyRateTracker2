package com.example.easyratetracker2.adapters

import androidx.navigation.Navigation
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.data.models.OuterDetailsModel
import com.example.easyratetracker2.data.models.RatesElementModel
import com.example.easyratetracker2.databinding.RatesElementBinding
import com.example.easyratetracker2.ui.ContentMainFragmentDirections
import javax.inject.Inject

open class TrackedRatesAdapter @Inject constructor(itemCallback: ItemCallback<RatesElementModel>) :
    StateDisplayAdapter<RatesElementModel, RatesElementBinding>(
        itemCallback,
        RatesElementBinding::inflate,
        this::setUpData
    ) {


    companion object{
        private fun setUpData(
            model: RatesElementModel,
            binding: RatesElementBinding
        ) {
            binding.model = model
            binding.root.setOnClickListener{ v ->
                Navigation.findNavController(v).navigate(
                    ContentMainFragmentDirections.actionContentMainToRateDetails(OuterDetailsModel(model))
                )
            }
        }
    }

}