package com.luridevlabs.citylights.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luridevlabs.citylights.databinding.FragmentAddNewListBinding

class AddNewListFragment : Fragment() {

    private lateinit var binding: FragmentAddNewListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewListBinding.inflate(layoutInflater)
        return binding.root
    }
}