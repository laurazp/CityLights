package com.luridevlabs.citylights.presentation.fragment.personallists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView.OnEditorActionListener
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

    private fun initUI() {
        monumentsViewModel.getAddPersonalListLiveData().observe(viewLifecycleOwner) { state ->
            if (state != null) handleNewListState(state)
        }

        binding.btnAddNewListButton.setOnClickListener {
            saveNewList()
        }
        binding.tiePersonalListTitle.setOnEditorActionListener { editText, actionId, event ->
            saveNewList()
            true
        }
    }

    private fun handleNewListState(state: AddPersonalListsState) {
        when(state) {
            is ResourceState.Loading -> {}
            is ResourceState.Success -> {
                Toast.makeText(requireContext(), "Success state!", Toast.LENGTH_LONG).show()
            }
            is ResourceState.Error -> {
                Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    /**Por agilidad no voy a comprobar que ya exista una lista con el mismo nombre*/
    private fun saveNewList() {
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