package com.luridevlabs.citylights.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.FragmentSelectedListBinding
import com.luridevlabs.citylights.presentation.adapter.PersonalListsAdapter
import com.luridevlabs.citylights.presentation.common.ResourceState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import com.luridevlabs.citylights.presentation.viewmodel.PersonalListsState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SelectedListFragment : Fragment() {

    private lateinit var binding: FragmentSelectedListBinding

    private val personalListsAdapter = PersonalListsAdapter()
    private val monumentsViewModel: MonumentsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectedListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initContent()
    }

    private fun initContent() {
        monumentsViewModel.getPersonalListsLiveData().observe(viewLifecycleOwner) { state ->
            if (state != null) handleMyListsState(state)
        }

        if (monumentsViewModel.getMonumentListLiveData().value == null) {
            monumentsViewModel.fetchPersonalLists()
        }
    }

    private fun handleMyListsState(state: PersonalListsState) {
        when(state) {
            is ResourceState.Loading -> {
                binding.pbSelectedList.visibility = View.VISIBLE
            }
            is ResourceState.Success -> {
                binding.pbSelectedList.visibility = View.GONE
                //personalListsAdapter.submitList(state.result)
            }
            is ResourceState.Error -> {
                binding.pbSelectedList.visibility = View.GONE
                showErrorDialog(state.error)
            }
            else -> {}
        }
    }

    private fun showErrorDialog(error: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton(R.string.acceptButtonText, null)
            .setNegativeButton(R.string.tryAgainButtonText) { dialog, witch ->
                monumentsViewModel.fetchMonuments()
            }
    }
}