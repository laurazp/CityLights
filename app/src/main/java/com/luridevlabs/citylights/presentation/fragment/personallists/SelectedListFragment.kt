package com.luridevlabs.citylights.presentation.fragment.personallists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.FragmentSelectedListBinding
import com.luridevlabs.citylights.presentation.common.ResourceState
import com.luridevlabs.citylights.presentation.fragment.personallists.adapter.MonumentListAdapter
import com.luridevlabs.citylights.presentation.viewmodel.MonumentListState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import com.luridevlabs.citylights.presentation.viewmodel.PersonalListsState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SelectedListFragment : Fragment() {

    private lateinit var binding: FragmentSelectedListBinding

    private val monumentListAdapter = MonumentListAdapter()
    private val monumentsViewModel: MonumentsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSelectedListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initContent()
    }

    private fun initContent() {
        monumentsViewModel.getMonumentListLiveData().observe(viewLifecycleOwner) { state ->
            if (state != null) handleMyListsState(state)
        }

        if (monumentsViewModel.getMonumentListLiveData().value == null) {
            monumentsViewModel.fetchPersonalLists()
        }
    }

    private fun handleMyListsState(state: MonumentListState) {
        when (state) {
            is ResourceState.Loading -> {
                binding.pbSelectedList.visibility = View.VISIBLE
            }

            is ResourceState.Success -> {
                binding.pbSelectedList.visibility = View.GONE
                monumentListAdapter.submitList(state.result)
            }

            is ResourceState.Error -> {
                binding.pbSelectedList.visibility = View.GONE
                showErrorDialog(state.error)
            }
        }
    }

    private fun showErrorDialog(error: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton(R.string.acceptButtonText, null)
            .setNegativeButton(R.string.tryAgainButtonText) { dialog, _ ->
                monumentsViewModel.fetchMonuments()
                dialog.dismiss()
            }
    }
}