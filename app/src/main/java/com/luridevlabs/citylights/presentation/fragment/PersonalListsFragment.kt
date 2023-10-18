package com.luridevlabs.citylights.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.FragmentPersonalListsBinding
import com.luridevlabs.citylights.presentation.MainActivity
import com.luridevlabs.citylights.presentation.adapter.PersonalListsAdapter
import com.luridevlabs.citylights.presentation.common.ResourceState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import com.luridevlabs.citylights.presentation.viewmodel.PersonalListsState
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class PersonalListsFragment : Fragment() {

    private lateinit var binding: FragmentPersonalListsBinding
    private val monumentsViewModel: MonumentsViewModel by activityViewModel()
    private val personalListsAdapter = PersonalListsAdapter()

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
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).setTitle(getString(R.string.personal_lists_title))
        //TODO: actualizar listas
    }

    private fun initUI() {
        binding.fabMyListsAddListButton.setOnClickListener {
            (activity as MainActivity).navigateTo(R.id.action_personalListsFragment_to_addNewListFragment)
        }

        monumentsViewModel.getPersonalListsLiveData().observe(viewLifecycleOwner) { state ->
            if (state != null) handlePersonalListsState(state)
        }
    }

    private fun handlePersonalListsState(state: PersonalListsState) {
        when(state) {
            is ResourceState.Loading -> {
                binding.pbPersonalListsProgressBar.visibility = View.VISIBLE
            }
            is ResourceState.Success -> {
                binding.pbPersonalListsProgressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Success state!", Toast.LENGTH_LONG).show()
                //TODO:
                personalListsAdapter.submitList(state.result)
            }
            is ResourceState.Error -> {
                binding.pbPersonalListsProgressBar.visibility = View.GONE
                //TODO: show dialog
                Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }
}