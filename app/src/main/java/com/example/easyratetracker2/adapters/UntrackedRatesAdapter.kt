package com.example.easyratetracker2.adapters

import android.view.ViewGroup
import androidx.navigation.NavController
import com.example.easyratetracker2.adapters.util.ItemCallback
import com.example.easyratetracker2.data.models.OuterDetailsModel
import com.example.easyratetracker2.data.models.RatesElementModel
import com.example.easyratetracker2.databinding.RatesElementBinding
import com.example.easyratetracker2.ui.ContentMainFragmentDirections
import javax.inject.Inject

class UntrackedRatesAdapter @Inject constructor(itemCallback: ItemCallback<RatesElementModel>) :
    StateDisplayAdapter<RatesElementModel>(itemCallback) {

    var navController: NavController? = null

    override val vhProducer: (parent: ViewGroup) -> ModelViewHolder<RatesElementModel, RatesElementBinding> =
        { parent ->
            ModelViewHolder(
                parent,
                RatesElementBinding::inflate,
                this::setUpData
            )
        }

    private fun setUpData(
        model: RatesElementModel,
        binding: RatesElementBinding
    ) {
        binding.model = model

        navController?.let { nc ->
            binding.root.setOnClickListener {
                nc.navigate(
                    ContentMainFragmentDirections.actionContentMainToRateDetails(
                        OuterDetailsModel(model)))
            }
        }
    }
}