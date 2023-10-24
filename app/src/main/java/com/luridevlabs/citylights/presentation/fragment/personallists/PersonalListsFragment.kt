package com.luridevlabs.citylights.presentation.fragment.personallists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.FragmentPersonalListsBinding
import com.luridevlabs.citylights.presentation.MainActivity
import com.luridevlabs.citylights.presentation.common.ResourceState
import com.luridevlabs.citylights.presentation.fragment.personallists.adapter.MonumentListAdapter
import com.luridevlabs.citylights.presentation.fragment.personallists.adapter.PersonalListAdapter
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import com.luridevlabs.citylights.presentation.viewmodel.PersonalListsState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PersonalListsFragment : Fragment() {

    private lateinit var binding: FragmentPersonalListsBinding
    private val monumentsViewModel: MonumentsViewModel by activityViewModel()
    private val personalListAdapter = PersonalListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonalListsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initContent()
    }

    private fun initUI() {
        binding.let { b ->
            b.rvPersonalListsView.layoutManager = LinearLayoutManager(this.context)
            b.rvPersonalListsView.adapter = personalListAdapter
        }

        binding.fabPersonalListsAddListButton.setOnClickListener {
            (activity as MainActivity).navigateTo(R.id.action_personalListsFragment_to_addNewListFragment)
        }
    }


    private fun initContent() {
        monumentsViewModel.getPersonalListsLiveData().observe(viewLifecycleOwner) { state ->
            if (state != null) handleMyListsState(state)
        }

        if (monumentsViewModel.getPersonalListsLiveData().value == null) {
            monumentsViewModel.fetchPersonalLists()
        }
    }

    private fun handleMyListsState(state: PersonalListsState) {
        when(state) {
            is ResourceState.Loading -> {
                binding.pbPersonalListsProgressBar.visibility = View.VISIBLE
            }
            is ResourceState.Success -> {
                binding.pbPersonalListsProgressBar.visibility = View.GONE
                personalListAdapter.submitList(state.result)
            }
            is ResourceState.Error -> {
                binding.pbPersonalListsProgressBar.visibility = View.GONE
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
                monumentsViewModel.fetchPersonalLists()
                dialog.dismiss()
            }
    }
}