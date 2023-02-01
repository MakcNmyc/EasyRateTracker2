package com.example.easyratetracker2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.easyratetracker2.R
import com.example.easyratetracker2.Settings
import com.example.easyratetracker2.data.repositories.utilities.StorageRequest
import com.example.easyratetracker2.data.store.room.TrackedRate
import com.example.easyratetracker2.databinding.RateDetailsBinding
import com.example.easyratetracker2.viewmodels.RateDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateDetails: Fragment() {

    val viewModel: RateDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = createBinding(inflater, container, RateDetailsBinding::inflate)

        binding.viewModel = viewModel
        binding.floatingActionButton.setOnClickListener{
            viewModel.addToTracked { operations ->
                showAddNotification(binding, operations)
            }
        }

        binding.checkBox.setOnCheckedChangeListener { button, v ->
            if (v) {
                viewModel.addToTracked { operations ->
                    showAddNotification(binding, operations)
                }
            } else {
                viewModel.deleteFromTracked { operations ->
                    Snackbar.make(
                        binding.floatingActionButton,
                        if (operations.isDeny) R.string.error_tracked_notification
                        else R.string.deleted_from_tracked_notification,
                        Settings.SNACKBAR_DURATION
                    ).show()
                }
            }
        }

        return binding.root
    }


    private fun showAddNotification(binding: RateDetailsBinding, operations: StorageRequest<TrackedRate>) {
        Snackbar.make(binding.floatingActionButton,
            if (operations.isDeny) R.string.error_tracked_notification
            else R.string.added_to_tracked_notification,
            Settings.SNACKBAR_DURATION
        ).show()
    }
}