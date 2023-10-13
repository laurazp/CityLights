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

    private fun initUI() {
        binding.fabMyListsAddListButton.setOnClickListener {
            Toast.makeText(context, "Button clicked!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_personalListsFragment_to_addNewListFragment)
        }
    }
}