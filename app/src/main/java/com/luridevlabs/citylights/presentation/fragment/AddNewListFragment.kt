package com.luridevlabs.citylights.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.FragmentAddNewListBinding
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

    private fun initUI() {
        val addListButton = binding.btnAddNewListButton

        monumentsViewModel.getAddPersonalListLiveData().observe(viewLifecycleOwner) { state ->
            if (state != null) handleNewListState(state)
        }

        if (monumentsViewModel.getAddPersonalListLiveData().value == null) {
            monumentsViewModel.fetchPersonalLists()
        }

        addListButton.setOnClickListener {
            saveList()
            Toast.makeText(requireContext(), "Lista añadida", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleNewListState(state: AddPersonalListsState) {
        when(state) {
            is ResourceState.Loading -> {
                //TODO
            }
            is ResourceState.Success -> {
                Toast.makeText(requireContext(), "Success state!", Toast.LENGTH_LONG).show()
                //this.findNavController().popBackStack()
            }
            is ResourceState.Error -> {
                //TODO
                Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    private fun saveList() {
        val newListName = binding.tiePersonalListTitle.text.toString()

        if (newListName.isNotBlank()) {
            monumentsViewModel.addNewList(newListName)
            //TODO: volver a PersonalListsFragment
            childFragmentManager.popBackStack()
        } else {
            //this.findNavController().popBackStack()
            Toast.makeText(requireContext(), "No se ha podido guardar la lista.", Toast.LENGTH_LONG).show()

        }
    }
}