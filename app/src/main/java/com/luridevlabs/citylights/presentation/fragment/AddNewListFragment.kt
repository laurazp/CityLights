package com.luridevlabs.citylights.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.FragmentAddNewListBinding
import com.luridevlabs.citylights.presentation.MainActivity
import com.luridevlabs.citylights.presentation.common.ResourceState
import com.luridevlabs.citylights.presentation.viewmodel.AddPersonalListsState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AddNewListFragment : Fragment() {

    private lateinit var binding: FragmentAddNewListBinding
    private val monumentsViewModel: MonumentsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).setTitle(getString(R.string.add_new_list_title))
    }

    private fun initUI() {
        val addListButton = binding.btnAddNewListButton

        monumentsViewModel.getAddPersonalListLiveData().observe(viewLifecycleOwner) { state ->
            if (state != null) handleNewListState(state)
        }

        /*if (monumentsViewModel.getAddPersonalListLiveData().value == null) {
            monumentsViewModel.fetchPersonalLists()
        }*/

        addListButton.setOnClickListener {
            saveList()
        }
    }

    private fun handleNewListState(state: AddPersonalListsState) {
        when(state) {
            is ResourceState.Loading -> {
                binding.pbPersonalListProgressBar.visibility = View.VISIBLE
            }
            is ResourceState.Success -> {
                binding.pbPersonalListProgressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Success state!", Toast.LENGTH_LONG).show()
            }
            is ResourceState.Error -> {
                binding.pbPersonalListProgressBar.visibility = View.GONE
                //TODO: show dialog
                Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    private fun saveList() {
        val newListName = binding.tiePersonalListTitle.text.toString()

        if (newListName.isNotBlank()) {
            monumentsViewModel.addNewList(newListName)
            Toast.makeText(requireContext(), getString(R.string.list_added_text), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(),
                getString(R.string.list_name_required_text), Toast.LENGTH_LONG).show()
        }
        (activity as MainActivity).navigateTo(-1)
    }
}