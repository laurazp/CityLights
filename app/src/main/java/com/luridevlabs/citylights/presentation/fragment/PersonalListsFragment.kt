package com.luridevlabs.citylights.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.FragmentPersonalListsBinding
import com.luridevlabs.citylights.presentation.MainActivity


class PersonalListsFragment : Fragment() {

    private lateinit var binding: FragmentPersonalListsBinding

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
            //TODO: revisar navegación !!
            //requireActivity().supportFragmentManager.beginTransaction()
                //.replace(R.id.fcv_main_container, AddNewListFragment()).commit()

        }
    }
}